package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.socialapp.R;

import pt.up.fc.progmovel.socialapp.util.Constants;

public class ChatActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        String mChatID = intent.getStringExtra(Constants.EXTRA_CHAT_ID);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_CHAT_ID, mChatID);

        FragmentManager fm = getSupportFragmentManager();

        Fragment inputChat = new ChatInputFragment();
        inputChat.setArguments(bundle);

        Fragment messagesList = new ChatMessageListFragment();
        messagesList.setArguments(bundle);

        fm.beginTransaction()
                    .replace(R.id.chat_message_input_placeholder, inputChat)
                    .replace(R.id.chat_messages_list_placeholder, messagesList)
                    .commit();

        }

}
