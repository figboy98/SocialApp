package pt.up.fc.progmovel.socialapp.ui.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.socialapp.R;

public class ChatActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        FragmentManager fm = getSupportFragmentManager();

        Fragment inputChat = new ChatInputFragment();

        Fragment messagesList = new ChatMessageListFragment();

        fm.beginTransaction()
                    .replace(R.id.chat_message_input_placeholder, inputChat)
                    .replace(R.id.chat_messages_list_placeholder, messagesList)
                    .commit();

        }

}
