package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.socialapp.R;

import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.ui.home.HomeViewModel;

public class ChatActivity extends FragmentActivity {
    private static final String EXTRA_CHAT_ID =  "pt.up.fc.progmovel.socialapp.extra.CHAT_ID";
    private String mChatID;
    private HomeViewModel homeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        mChatID = intent.getStringExtra(EXTRA_CHAT_ID);


        SocialAppRepository repository = new SocialAppRepository(getApplication());

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CHAT_ID, mChatID);

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
