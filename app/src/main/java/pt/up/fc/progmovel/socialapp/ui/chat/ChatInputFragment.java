package pt.up.fc.progmovel.socialapp.ui.chat;

import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.ChatRepository;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;


public class ChatInputFragment extends Fragment {
    private ChatRepository mChatRepository;
    private String mChatID;
    private EditText mInputMessage;
    private final int GET_IMAGE_CODE = 1;
    private static final String EXTRA_CHAT_ID = "pt.up.fc.progmovel.socialapp.extra.CHATID";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatMessage chatMessage = new ChatMessage();
        mChatRepository = new ChatRepository(requireActivity().getApplication());
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
                ChatMessage message = new ChatMessage(mInputMessage.getText().toString(), date, "me", "to", "text");
                mChatRepository.insertChatMessage(message, mChatID);
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
                                ChatMessage message = new ChatMessage(imagePath, date, "me", "to", messageType);
                                mChatRepository.insertChatMessage(message, mChatID);
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