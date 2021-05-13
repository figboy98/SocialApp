package pt.up.fc.progmovel.socialapp.ui.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.socialapp.R;

import java.util.Date;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.ChatRepository;
import pt.up.fc.progmovel.socialapp.database.GroupChat;
import pt.up.fc.progmovel.socialapp.database.GroupChatMessagesCrossRef;

public class ChatActivity extends FragmentActivity {
    private ChatRepository repository;
    private static final String EXTRA_CHATID =  "pt.up.fc.progmovel.socialapp.extra.CHATID";
    private String mChatID;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        repository = new ChatRepository(getApplication());

        GroupChat chat = new GroupChat("test");

        repository.insertGroupChat(chat);

        String chatID = chat.getGroupChatID();
        mChatID = chatID;

        /*for(int i=0; i<10; i++){
            Date date = new Date();

            String f;
            String t;
            if(i%2==0){
                f = "me";
                t=  "you";
            }
            else{
                f="you";
                t="me";
            }

            ChatMessage mSent = new ChatMessage("Hello " + i, date, f, t, "text");
            repository.insertChatMessage(mSent, chatID);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }*/
       /* Date date = new Date();

        repository.insertChatMessage(new ChatMessage("@drawable/default_image",date,  "me", "to", "image"));
        date = new Date();


        repository.insertChatMessage(new ChatMessage("@drawable/default_image",date,  "to", "me","image"));
        date = new Date();

        repository.insertChatMessage(new ChatMessage("@drawable/default_image",date,  "me", "to","video"));
        date = new Date();

        repository.insertChatMessage(new ChatMessage("@drawable/default_image",date,  "to", "me", "video"));*/

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
