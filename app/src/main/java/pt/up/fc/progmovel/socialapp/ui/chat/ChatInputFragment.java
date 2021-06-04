package pt.up.fc.progmovel.socialapp.ui.chat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import pt.up.fc.progmovel.socialapp.MainActivity;
import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.util.BluetoothService;
import pt.up.fc.progmovel.socialapp.util.Constants;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;


public class ChatInputFragment extends Fragment {
    private static BluetoothService mBluetoothService;
    private static SocialAppRepository mSocialAppRepository;
    private String mChatID;
    private EditText mInputMessage;
    private final int GET_IMAGE_CODE = 1;

    private Boolean mBound;
    private String localUserId;
    private ServiceConnection connection;
    private  View view;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatMessage chatMessage = new ChatMessage();
        mSocialAppRepository = new SocialAppRepository(requireActivity().getApplication());

        if (getArguments() != null) {
            mChatID = getArguments().getString(Constants.EXTRA_CHAT_ID);
        }

        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        localUserId = preferences.getString(Constants.SHARED_LOCAL_USER_ID, "");


        connection = new ServiceConnection() {

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

        Intent intent = new Intent(requireContext(), BluetoothService.class);
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_input, container, false);

        mInputMessage = view.findViewById(R.id.chat_message_input);
        ImageButton sendButton = view.findViewById(R.id.send_image_button);
        ImageButton imagesButton = view.findViewById(R.id.image_input_button);
        ImageButton videosButton = view.findViewById(R.id.video_input_button);

        imagesButton.setOnClickListener(new ImagesButtonClickListener("image/*"));
        videosButton.setOnClickListener(new ImagesButtonClickListener("video/*"));

        sendButton.setOnClickListener(new SentButtonClickListener());

        return view;
    }

    private class SentButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mInputMessage.toString().length() > 0) {
                Date date = new Date();
                ChatMessage message = new ChatMessage(mInputMessage.getText().toString(), date, localUserId, mChatID, "text");
                new SendMessageAsyncTask(message).execute(requireActivity());
            }
            mInputMessage.setText("");
        }
    }

    private class ImagesButtonClickListener implements View.OnClickListener {
        private final String mType;

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
                                new SendMessageAsyncTask(message).execute(requireActivity());
                            }
                        }

                    }
                });

        @Override
        public void onClick(View v) {
            mGetContent.launch(mType);
        }
    }
    private static class SendMessageAsyncTask extends AsyncTask<Activity, Void, Void> {
        ChatMessage message;
        String type;

        SendMessageAsyncTask(ChatMessage message) {
            this.message = message;
            type = message.getType();
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            boolean sent=false;
            if (type.equals("text")) {
                byte[] messageByte = message.getByte();
                sent = mBluetoothService.write(messageByte, Constants.BLUETOOTH_TYPE_CHAT_MESSAGE);

            }
            else if(type.equals("image")){
                mSocialAppRepository.insertChatMessage(message);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(activities[0].getContentResolver(), Uri.parse(message.getTextMessage()));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();

                    /*Save media bytes to the message to make it easier to send with Bluetooth */

                    message.setByte(data);

                    sent = mBluetoothService.write(message.getByte(), Constants.BLUETOOTH_TYPE_CHAT_MESSAGE);

                    /*It's not necessary to keep the media bytes in the message, it's just used to send with bluetooth
                    then its saved to the storage */

                    message.setByte(new byte[0]);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (sent) {
                mSocialAppRepository.insertChatMessage(message);
            }
            return null;
        }
    }
}