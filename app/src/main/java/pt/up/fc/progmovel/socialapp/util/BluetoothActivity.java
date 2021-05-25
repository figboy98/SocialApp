package pt.up.fc.progmovel.socialapp.util;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_CONNECT_DEVICE =1;
    private static final int REQUEST_ENABLE_BT =2;
    private static final int REQUEST_ENABLE_LOCATION =3;
    private static final int REQUEST_DISCOVERY = 4;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mBluetoothServive = null;
    private ActivityResultLauncher<String> requestBluetoothPermission;
    private ActivityResultLauncher<String> requestBTDiscovery;
    private ActivityResultLauncher<String> requestCoarseLocation;
    private ActivityResultLauncher<String> requestFineLocation;

    boolean bluetoothUsePermission = false;
    boolean bluetoothDiscoveryPermission = false;
    boolean coarseLocationPermission = false;
    boolean fineLocationPermission = false;


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







    }
    @Override
    public void onStart(){
        super.onStart();

        //Request necessary permissions to be able to use Bluetooth
        checkPermissions();
        if (!mBluetoothAdapter.isEnabled()) {
            startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }else  if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent intentDiscover = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intentDiscover.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intentDiscover);
        }
        else if (mBluetoothServive == null) {
        }

        finish();


    }

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
    }

}
