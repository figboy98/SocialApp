package pt.up.fc.progmovel.socialapp.ui.chat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.socialapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.util.BluetoothService;
import pt.up.fc.progmovel.socialapp.util.Constants;


public class ChatInputFragment extends Fragment {
    private static final String TAG = "BLUETOOTH_SERVICE";
    private static BluetoothService mBluetoothService;
    private static SocialAppRepository mSocialAppRepository;
    private String mChatID;
    private EditText mInputMessage;
    private String localUserId;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocialAppRepository = new SocialAppRepository(requireActivity().getApplication());

        if (getArguments() != null) {
            mChatID = getArguments().getString(Constants.EXTRA_CHAT_ID);
        }

        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        localUserId = preferences.getString(Constants.SHARED_LOCAL_USER_ID, "");


        ServiceConnection connection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
                mBluetoothService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
            }
        };

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
                                Uri uri = result.get(i);
                                String imagePath = uri.toString();
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
    private static class SendMessageAsyncTask extends AsyncTask<Activity,Void,Void>{
        ChatMessage message;
        String type;

        public SendMessageAsyncTask(ChatMessage chatMessage){
            message = chatMessage;
            type = chatMessage.getType();
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            boolean sent=false;
            switch (type) {
                case "text":
                    byte[] messageByte = message.getByte();
                    sent = mBluetoothService.write(messageByte, Constants.BLUETOOTH_TYPE_CHAT_MESSAGE);

                    break;
                case "image":
                    try {
                        ContentResolver contentResolver = activities[0].getContentResolver();
                        String path= message.getTextMessage();
                        Uri uri = Uri.parse(path);

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(activities[0].getContentResolver(), uri);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        /*Save media bytes to the message to make it easier to send with Bluetooth */

                        message.setByte(data);

                        sent = mBluetoothService.write(message.getByte(), Constants.BLUETOOTH_TYPE_CHAT_MESSAGE);
                        if(!sent){
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activities[0].getApplicationContext(), "Doesn't have a bluetooth connection", Toast.LENGTH_LONG).show();
                                    ;
                                }
                            });
                        }
                        else{
                            message.setByte(new byte[0]);
                            mSocialAppRepository.insertChatMessage(message);

                        }


                    /*It's not necessary to keep the media bytes in the message, it's just used to send with bluetooth
                    then its saved to the storage */


                    } catch (IOException e) {
                        Log.d(TAG, "Error transforming image in bytes: " + e);
                    }

                    break;
                case "video":
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Uri uri = Uri.parse(message.getTextMessage());
                        InputStream inputStream = activities[0].getContentResolver().openInputStream(uri);
                        byte[] buffer = new byte[1024];
                        int n;
                        while (-1 != (n = inputStream.read(buffer)))
                            baos.write(buffer, 0, n);

                        byte[] data = baos.toByteArray();
                        message.setByte(data);
                        sent = mBluetoothService.write(message.getByte(), Constants.BLUETOOTH_TYPE_CHAT_MESSAGE);
                        message.setByte(new byte[0]);
                    } catch (IOException e) {
                        Log.d(TAG, "Error transforming video in bytes: " + e);
                    }
                    break;
            }
            if (sent) {
                mSocialAppRepository.insertChatMessage(message);
            }
            else {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activities[0].getApplicationContext(), "Doesn't have a bluetooth connection", Toast.LENGTH_LONG).show();
                        ;
                    }
                });
            }
            return null;
        }
    }
}