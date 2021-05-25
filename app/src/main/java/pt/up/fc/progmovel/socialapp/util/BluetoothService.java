package pt.up.fc.progmovel.socialapp.util;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.socialapp.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

public class BluetoothService extends Service {
    private static final String TAG = "BLUETOOTH_LE";
    private static final UUID id = UUID.fromString("e526a16e-f365-472a-87e3-a219d75ff262");
    private ParcelUuid mParcelUuid = new ParcelUuid(id);
    private static BluetoothAdapter bt = null;
    private static ArrayList<BluetoothDevice> mBTDevices = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothManager mBluetoothManager;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private Boolean mScanning;
    private Handler mHandler;

    private static final long SCAN_PERIOD = 10000;

    private ScanCallback mScanCallback = new LeScanCallback();

    private List<ScanFilter> mScanFilter = buildScanFilters();


    private ScanSettings mScanSettings = new ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .build();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String CHANNEL_ID = "SocialAppNotification";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_search_24)
                .setContentTitle("SocialApp")
                .setContentText("Searching for data")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.build();

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mHandler = new Handler();
        startAdvertising();
        scanLeDevice(true);
        return Service.START_REDELIVER_INTENT;

    }

    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setServiceUuid(mParcelUuid);
        scanFilters.add(builder.build());

        return scanFilters;
    }

    private AdvertiseSettings buildAdvertisingSettings(){
        AdvertiseSettings.Builder settings = new AdvertiseSettings.Builder();
        settings.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        settings.setTimeout(0);
        return settings.build();
    }

    private AdvertiseData buildAdvertiseData(){
        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.addServiceUuid(mParcelUuid);
        return dataBuilder.build();
    }

    private class LeAdvertiseCallBack extends AdvertiseCallback{
        @Override
        public void onStartFailure(int errorCode){
            super.onStartFailure(errorCode);
            String errorReason = "";
            Log.d(TAG, "Advertising Failed ");
            switch (errorCode){
                case AdvertiseCallback.ADVERTISE_FAILED_ALREADY_STARTED:
                    errorReason = "Already Started";
                    break;
                case AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE:
                    errorReason = "Data too large";
                    break;
                case AdvertiseCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED:
                    errorReason = "Featured Unsupported";
                    break;
                case AdvertiseCallback.ADVERTISE_FAILED_INTERNAL_ERROR:
                    errorReason = "Internal error";
                    break;
                case AdvertiseCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
                    errorReason = "Too many advertisers";
                    break;
            }
            Log.d(TAG, "Advertising Failed: "  + errorReason);
        }

        @Override
        public void onStartSuccess(AdvertiseSettings settings){
            super.onStartSuccess(settings);
            Log.d(TAG, "Advertising started");
        }

    }

    private void startAdvertising(){
        Log.d(TAG, "Started Advertising");
        AdvertiseSettings settings = buildAdvertisingSettings();
        AdvertiseData data = buildAdvertiseData();
        AdvertiseCallback callback = new LeAdvertiseCallBack();

        if(mBluetoothLeAdvertiser !=null){
            mBluetoothLeAdvertiser.startAdvertising(settings, data, callback);
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothLeScanner.stopScan(mScanCallback);
                    Log.d(TAG, "Stopping Scan");
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothLeScanner.startScan(mScanFilter, mScanSettings, mScanCallback);
            Log.d(TAG, "Starting Scan");


        } else {
            mScanning = false;
            mBluetoothLeScanner.stopScan(mScanCallback);
            Log.d(TAG, "Stopping Scan");
        }
    }

    private class LeScanCallback extends ScanCallback {

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

            for (ScanResult result : results) {
                Log.d(TAG, result.toString());
            }
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "Result " +  result.toString());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    }
}
