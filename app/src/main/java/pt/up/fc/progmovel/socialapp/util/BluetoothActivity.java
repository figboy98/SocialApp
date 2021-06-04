package pt.up.fc.progmovel.socialapp.util;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class BluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_CONNECT_DEVICE =1;
    private static final int REQUEST_ENABLE_BT =2;
    private static final int REQUEST_ENABLE_LOCATION =3;
    private static final int REQUEST_DISCOVERY = 4;
    private static  final int REQUEST_WRITE = 5;
    private static final int REQUEST_READ = 6;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mBluetoothServive = null;
    private LocationManager mLocationManager = null;
    private ActivityResultLauncher<String> requestBluetoothPermission;
    private ActivityResultLauncher<String> requestBTDiscovery;
    private ActivityResultLauncher<String> requestCoarseLocation;
    private ActivityResultLauncher<String> requestFineLocation;
    private ActivityResultLauncher<String> requestWritePermission;
    private ActivityResultLauncher<String> requestReadPermission;
    boolean bluetoothUsePermission = false;
    boolean bluetoothDiscoveryPermission = false;
    boolean coarseLocationPermission = false;
    boolean fineLocationPermission = false;
    boolean writePermission = false;
    boolean readPermission = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }
        requestBluetoothPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                    bluetoothUsePermission = true;
            }

        });

        requestBluetoothPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
            if(isGranted){
                bluetoothDiscoveryPermission = true;
            }
        });

        requestCoarseLocation = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
            if(isGranted){
                coarseLocationPermission = true;
            }
        });

        requestFineLocation = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
            if(isGranted){
                fineLocationPermission = true;
            }
        });

        requestWritePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
            if(isGranted){
                writePermission = true;
            }
        });
        requestReadPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted ->{
            if(isGranted){
                readPermission = true;
            }
        });

    }
    @Override
    public void onStart(){
        super.onStart();

        //Request necessary permissions to be able to use Bluetooth
        checkPermissions();

        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (!mBluetoothAdapter.isEnabled()) {
            startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
        else  if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//            Intent intentDiscover = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            intentDiscover.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//            startActivity(intentDiscover);
        }

        else if (mBluetoothServive == null) {
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if(!mLocationManager.isLocationEnabled()){
                Intent location = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(location);
            }
        }


        finish();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

 /*   private void enableLocationSettings() {

        Activity activity = this;

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity,
                                101);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }*/



    private void checkPermissions(){


        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED){

            bluetoothUsePermission = true;
        }
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED){

            bluetoothDiscoveryPermission = true;
        }
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            coarseLocationPermission = true;
        }

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_CHECKIN_PROPERTIES) == PackageManager.PERMISSION_GRANTED){
            fineLocationPermission = true;
        }

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            writePermission = true;
        }
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            readPermission = true;
        }


        if(!bluetoothUsePermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH)) {
                    //Inform the user of the necessity of this permission
                }
            }
            requestBluetoothPermission.launch(Manifest.permission.BLUETOOTH);
        }
        if(!bluetoothDiscoveryPermission){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_ADMIN)){
                    //Inform the user of the necessity of this permission
                }
            }
            requestBTDiscovery.launch(Manifest.permission.BLUETOOTH_PRIVILEGED);


        }

        if(!coarseLocationPermission){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    //Inform the user of the necessity of this permission
                }
            }
            requestCoarseLocation.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if(!fineLocationPermission){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    //Inform the user of the necessity of this permission
                }
            }
            requestFineLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        }
        if(!writePermission){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    //Inform the user of the necessity of this permission
                }
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE);

           // requestWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!readPermission){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    //Inform the user of the necessity of this permission
                }
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ);

           // requestWritePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

}
