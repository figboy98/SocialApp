package pt.up.fc.progmovel.socialapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialapp.R;

import pt.up.fc.progmovel.socialapp.MainActivity;
import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.database.User;
import pt.up.fc.progmovel.socialapp.util.BluetoothActivity;
import pt.up.fc.progmovel.socialapp.util.BluetoothService;
import pt.up.fc.progmovel.socialapp.util.Constants;

public class Login extends AppCompatActivity {
    private String name;
    private EditText nameInput;
    private SocialAppRepository mSocialAppRepository;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mSocialAppRepository = new SocialAppRepository(this.getApplication());
        nameInput = findViewById(R.id.login_name_input);
        Button loginButton = findViewById(R.id.login_button);

        Intent bluetooth = new Intent(this, BluetoothActivity.class);
        startActivity(bluetooth);

        Intent communication = new Intent(this, BluetoothService.class);
        this.startService(communication);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (nameInput.toString().length() > 0) {
                    name = nameInput.getText().toString();

                    mUser = mSocialAppRepository.getUser(name);
                    if (mUser != null) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Intent resultIntent = new Intent();
                        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Constants.SHARED_LOCAL_USER_ID, mUser.getUserId()).apply();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Wrong Username", Toast.LENGTH_LONG).show();
                        nameInput.setText("");
                    }

                }
            }
        });
    }

}
