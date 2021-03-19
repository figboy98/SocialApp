package pt.up.fc.progmovel.socialapp.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.socialapp.R;
import java.util.Calendar;
import java.util.Date;


public class ChatInputFragment extends Fragment {
    private ChatMessage mChatMessage;
    private EditText inputMessage;
    private ImageButton sendButton;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mChatMessage = new ChatMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_input, container, false);

        inputMessage = view.findViewById(R.id.chat_message_input);
        sendButton = view.findViewById(R.id.send_image_button);

        return view;
    }



}