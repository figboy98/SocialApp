package pt.up.fc.progmovel.socialapp.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialapp.R;

import java.util.Date;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.ChatRepository;
import pt.up.fc.progmovel.socialapp.database.GroupChat;
import pt.up.fc.progmovel.socialapp.database.GroupChatMessagesCrossRef;
import pt.up.fc.progmovel.socialapp.ui.home.HomeViewModel;

public class ChatActivity extends FragmentActivity {
    private ChatRepository repository;
    private static final String EXTRA_CHATID =  "pt.up.fc.progmovel.socialapp.extra.CHATID";
    private String mChatID;
    private HomeViewModel homeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        repository = new ChatRepository(getApplication());

        /*GroupChat chat = new GroupChat("test");

        repository.insertGroupChat(chat);

        String chatID = chat.getGroupChatID();
        mChatID = chatID;*/

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CHATID, mChatID);

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
