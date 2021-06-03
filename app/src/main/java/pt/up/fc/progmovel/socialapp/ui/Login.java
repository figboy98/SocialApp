package pt.up.fc.progmovel.socialapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.socialapp.R;

import java.util.List;

import pt.up.fc.progmovel.socialapp.MainActivity;
import pt.up.fc.progmovel.socialapp.database.ChatDao;
import pt.up.fc.progmovel.socialapp.database.ChatDatabase;
import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.ChatRepository;
import pt.up.fc.progmovel.socialapp.database.GroupChatWithMessages;
import pt.up.fc.progmovel.socialapp.database.User;

public class Login extends AppCompatActivity {
    private String name;
    private EditText nameInput;
    private Button loginButton;
    private ChatRepository mChatRepository;
    private User mUser;
    private ChatDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mChatRepository = new ChatRepository(this.getApplication());
        nameInput = findViewById(R.id.login_name_input);
        loginButton = findViewById(R.id.login_button);

//        final Observer<User> userObserver = user -> {
//            if(user !=null){
//                mUser = user;
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("EXTRA_USER_ID", user.getUserID());
//                setResult(MainActivity.RESULT_OK);
//                finish();
//            }
//            else{
//                Toast.makeText(Login.this, "Wrong Username", Toast.LENGTH_LONG).show();
//                nameInput.setText("");
//            }
//        };


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameInput.toString().length()>0){
                    name = nameInput.toString();

                    mUser = mChatRepository.getUser(name);
                    Log.d("Login", "sda");
                }

               //mChatRepository.getUser(name).observe((LifecycleOwner) getLifecycle(), userObserver);
            }
        });
    }

}
