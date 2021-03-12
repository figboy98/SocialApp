package pt.up.fc.progmovel.socialapp.ui.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.socialapp.R;

import pt.up.fc.progmovel.socialapp.ui.chat.ChatMessage;

public class ChatMessageFragment  extends Fragment {

    private ChatMessage mChatMessage;
    private EditText mChatText;

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        mChatMessage = new ChatMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        mChatText = (EditText) v.findViewById(R.id.chat_message_input);
        mChatText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mChatMessage.setTextMessage(s.toString());
                Toast toast = Toast.makeText(getContext(),"Clicked", Toast.LENGTH_LONG);
                toast.show();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;

    }


}
