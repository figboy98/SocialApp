package pt.up.fc.progmovel.socialapp.util;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsActivity extends AppCompatActivity {
    private static  final int REQUEST_WRITE = 5;
    private static final int REQUEST_READ = 6;
    private BluetoothAdapter mBluetoothAdapter = null;
    private ActivityResultLauncher<String> requestBluetoothPermission;
    private ActivityResultLauncher<String> requestCoarseLocation;
    private ActivityResultLauncher<String> requestFineLocation;
    private ActivityResultLauncher<String> requestBluetoothAdminPermission;
    private ActivityResultLauncher<String> requestReadPermission;
    private ActivityResultLauncher<String> requestWritePermission;

    boolean bluetoothUsePermission = false;
    boolean bluetoothAdminPermission = false;
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
        requestBluetoothAdminPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
            if(isGranted){
                bluetoothAdminPermission = true;
            }
        });
        requestBluetoothPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                    bluetoothUsePermission = true;
            }

        });

        requestBluetoothPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
            if(isGranted){
                bluetoothAdminPermission = true;
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
        requestWritePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted ->{
            if(isGranted){
                writePermission = true;
            }
        });
        requestReadPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
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

        LocationManager mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (!mBluetoothAdapter.isEnabled()) {
            startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
//        else  if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//            Intent intentDiscover = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            intentDiscover.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//            startActivity(intentDiscover);
//        }

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


    private void checkPermissions(){


        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED){

            bluetoothUsePermission = true;
        }
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED){

            bluetoothAdminPermission = true;
        }
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            coarseLocationPermission = true;
        }

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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
        if(!bluetoothAdminPermission){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_ADMIN)){
                    //Inform the user of the necessity of this permission
                }
            }
            requestBluetoothAdminPermission.launch(Manifest.permission.BLUETOOTH_PRIVILEGED);


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
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
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
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE);
            requestWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!readPermission){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    //Inform the user of the necessity of this permission
                }
            }
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ);
            requestReadPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

}
