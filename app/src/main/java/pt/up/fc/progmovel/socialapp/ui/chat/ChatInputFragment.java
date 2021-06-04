package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.socialapp.R;

import java.util.Date;
import java.util.List;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.util.BluetoothService;
import pt.up.fc.progmovel.socialapp.util.Constants;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;


public class ChatInputFragment extends Fragment {
    private BluetoothService mBluetoothService;
    private SocialAppRepository mSocialAppRepository;
    private String mChatID;
    private EditText mInputMessage;
    private final int GET_IMAGE_CODE = 1;
    private static final String EXTRA_CHAT_ID = "pt.up.fc.progmovel.socialapp.extra.CHAT_ID";
    private final String LOCAL_USER_UUID = "pt.up.fc.progmovel.socialapp.extra.USER_ID";

    private Boolean mBound;
    private String localUserId;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatMessage chatMessage = new ChatMessage();
        mSocialAppRepository = new SocialAppRepository(requireActivity().getApplication());
        if(getArguments()!=null){
            mChatID = getArguments().getString(EXTRA_CHAT_ID);
        }
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);

        localUserId = preferences.getString(LOCAL_USER_UUID, "");

        Intent intent = new Intent(requireContext(), BluetoothService.class);
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_input, container, false);

        mInputMessage = view.findViewById(R.id.chat_message_input);
        ImageButton sendButton = view.findViewById(R.id.send_image_button);
        ImageButton imagesButton = view.findViewById(R.id.image_input_button);
        ImageButton videosButton = view.findViewById(R.id.video_input_button);

        imagesButton.setOnClickListener(new ImagesButtonClickListener("image/*"));
        videosButton.setOnClickListener(new ImagesButtonClickListener("video/*"));

        sendButton.setOnClickListener(new SentButtonClickListener());

        return view;
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
            mBluetoothService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private class SentButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mInputMessage.toString().length() > 0) {
                Date date = new Date();
                ChatMessage message = new ChatMessage(mInputMessage.getText().toString(), date, localUserId, mChatID, "text");
                mSocialAppRepository.insertChatMessage(message);
                byte[] teste = new byte[10000];
                message.setByte(teste);
                byte[] messageByte = message.getByte();
                mBluetoothService.write(messageByte,Constants.BLUETOOTH_TYPE_CHAT_MESSAGE);
            }
            mInputMessage.setText("");


        }
    }

    private class ImagesButtonClickListener implements View.OnClickListener {
        private String mType;

        public ImagesButtonClickListener(String type) {
            mType = type;
        }

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(),
                new ActivityResultCallback<List<Uri>>() {
                    @Override
                    public void onActivityResult(List<Uri> result) {
                        String messageType;
                        if (mType.equals("image/*")) {
                            messageType = "image";
                        } else {
                            messageType = "video";
                        }

                        if (result.size() > 0) {
                            for (int i = 0; i < result.size(); i++) {
                                Date date = new Date();
                                String imagePath = result.get(i).toString();
                                ChatMessage message = new ChatMessage(imagePath, date, localUserId, mChatID, messageType);
                                mSocialAppRepository.insertChatMessage(message);
                            }
                        }

                    }
                });

        @Override
        public void onClick(View v) {
            mGetContent.launch(mType);
        }
    }
}