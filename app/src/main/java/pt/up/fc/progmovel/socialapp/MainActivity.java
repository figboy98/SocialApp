package pt.up.fc.progmovel.socialapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.socialapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import pt.up.fc.progmovel.socialapp.ui.Login;
import pt.up.fc.progmovel.socialapp.util.BluetoothActivity;
import pt.up.fc.progmovel.socialapp.util.BluetoothService;
import pt.up.fc.progmovel.socialapp.util.Constants;

public class MainActivity extends AppCompatActivity {
    private static final int BLUETOOTH_PERMISSION = 1;
    private static final int LOCATION_PERMISSION = 2;
    private String mLocalUserId;
    private BluetoothService mBluetoothService;
    private Constants mConstants;


    BluetoothAdapter bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConstants = new Constants();
        Intent login = new Intent(this, Login.class);

        SharedPreferences preferences = getSharedPreferences(mConstants.SHARED_PREFERENCES,Context.MODE_PRIVATE);

        mLocalUserId = preferences.getString(mConstants.SHARED_LOCAL_USER_ID, "");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_posts, R.id.navigation_chat)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Intent bluetooth = new Intent(this, BluetoothActivity.class);
        startActivity(bluetooth);

    }
}