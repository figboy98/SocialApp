package pt.up.fc.progmovel.socialapp.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialapp.R;
import java.util.Calendar;
import java.util.Date;


public class ChatFragment extends Fragment {
    private ChatMessage mChatMessage;
    private EditText inputMessage;
    private ImageButton sendButton;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mChatMessage = new ChatMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        inputMessage = v.findViewById(R.id.chat_message_input);
        sendButton = v.findViewById(R.id.send_image_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                String text = inputMessage.getText().toString();
                String from = "me";
                String to = "me";
                mChatMessage = new ChatMessage(text, currentTime, from,to);
            }
        });

        return v;
    }

    private void sendMessage(){

    }


}