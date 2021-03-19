package pt.up.fc.progmovel.socialapp.ui.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.socialapp.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FragmentManager fm = getSupportFragmentManager();

        Fragment inputChat = new ChatInputFragment();

        Fragment messagesList = new ChatMessageListFragment();



        /*fm.beginTransaction()
                    .add(R.id.container, inputChat)
                    .commit();
        }*/


            fm.beginTransaction()
                    .add(R.id.container, messagesList)
                    .commit();
        }
   // }

}
