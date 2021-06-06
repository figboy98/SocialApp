package pt.up.fc.progmovel.socialapp.ui.posts.input;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialapp.R;

import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.database.User;
import pt.up.fc.progmovel.socialapp.database.Post;
import pt.up.fc.progmovel.socialapp.util.BluetoothService;
import pt.up.fc.progmovel.socialapp.util.Constants;

public class PostsInput extends AppCompatActivity {

    private String content;
    private EditText postsInput;
    private SocialAppRepository mSocialAppRepository;
    private User mUser;
    private String userId;
    private BluetoothService mBluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_input);
        mSocialAppRepository = new SocialAppRepository(this.getApplication());
        postsInput = findViewById(R.id.post_add_content_text);
        Button postButton = findViewById(R.id.post_button);

       /* Intent bluetooth = new Intent(this, BluetoothActivity.class);
        startActivity(bluetooth);*/
//
//        Intent communication = new Intent(this, BluetoothService.class);
//        this.startService(communication);

        ServiceConnection connection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
                mBluetoothService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
            }
        };

        Intent intent = new Intent(getApplicationContext(), BluetoothService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        SharedPreferences preferences = getApplication().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        userId = preferences.getString(Constants.SHARED_LOCAL_USER_ID, "");

        postButton.setOnClickListener(v -> {
            if (!postsInput.getText().toString().isEmpty()) {
                content = postsInput.getText().toString();
                mUser = mSocialAppRepository.getUserFromId(userId);
                if (mUser != null) {
                    Post post = new Post(System.currentTimeMillis(), userId, content);
                    mSocialAppRepository.insertPost(post);
                    Toast.makeText(PostsInput.this, "Post added", Toast.LENGTH_LONG).show();
                    mBluetoothService.write(post.getByte(),Constants.BLUETOOTH_TYPE_POST);
                    finish();
                }
            }else{
                Toast.makeText(PostsInput.this, "The post is empty", Toast.LENGTH_LONG).show();
            }
        });
    }

}
