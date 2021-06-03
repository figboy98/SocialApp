package pt.up.fc.progmovel.socialapp.ui;

import android.content.Intent;
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameInput.toString().length() > 0) {
                    name = nameInput.getText().toString();

                    mUser = mSocialAppRepository.getUser(name);
                    if (mUser != null) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("EXTRA_USER_ID", mUser.getUserID());
                        setResult(MainActivity.RESULT_OK);
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
