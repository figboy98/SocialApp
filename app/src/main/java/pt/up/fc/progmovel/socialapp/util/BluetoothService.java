package pt.up.fc.progmovel.socialapp.util;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.ListActivity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

public class BluetoothService extends Service {
    private static final String TAG = "BLUETOOTH_LE";
    private static final UUID SERVICE_UUID = UUID.fromString("e526a16e-f365-472a-87e3-a219d75ff262");
    private static final UUID MESSAGE_UUID = UUID.fromString("83e9cc9e-bd68-11eb-8529-0242ac130003");
    private static final UUID CONFIRM_UUID = UUID.fromString("e1fa2eaa-bebc-11eb-8529-0242ac130003");
    private static final ParcelUuid mParcelUuid = new ParcelUuid(SERVICE_UUID);
    private static final BluetoothAdapter bt = null;
    private static final ArrayList<BluetoothDevice> mBTDevices = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothManager mBluetoothManager;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private BluetoothGattServer mGattServer;
    private BluetoothGattServerCallback mGattServerCallBack;
    private BluetoothGatt mGattClient;
    private BluetoothGattCallback mGattClientCallBack;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic;
    private Boolean mScanning;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private static final long DELAY_PERIOD = 10000;

    @Override
    public void onCreate(){
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mGattServerCallBack = new GattServerCallBack();
        mGattClientCallBack = new GattClientCallBack();
        mHandler = new Handler();
    }


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

        mGattServer = mBluetoothManager.openGattServer(this, mGattServerCallBack);
        mGattServer.addService(setupGattService());

        setupGattServer(this);
        startAdvertising();
        scanLeDevice(true);
        return Service.START_REDELIVER_INTENT;

    }

    private void setupGattServer(Context context){

        BluetoothGattServer gattServer = mBluetoothManager.openGattServer(context, mGattServerCallBack);
        BluetoothGattService service = setupGattService();

        gattServer.addService(service);
    }

    private BluetoothGattService setupGattService(){
        BluetoothGattService service = new BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        BluetoothGattCharacteristic messageCharacteristic = new BluetoothGattCharacteristic(MESSAGE_UUID,
                BluetoothGattCharacteristic.PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_WRITE);

        service.addCharacteristic(messageCharacteristic);

        BluetoothGattCharacteristic confirmCharacteristic = new BluetoothGattCharacteristic(CONFIRM_UUID,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);

        service.addCharacteristic(confirmCharacteristic);

        return  service;


    }
    public void sendMessage(ChatMessage message){
        Log.d(TAG, "Send Message");
        byte[] data = message.getByte();
        mBluetoothGattCharacteristic.setValue(data);
        mBluetoothGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);

        boolean success = mGattClient.writeCharacteristic(mBluetoothGattCharacteristic);

        if(success){
            Log.d(TAG, "Message Sent");
        }
        else{
            Log.d(TAG, "Message not sent");
        }

    }

    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setServiceUuid(mParcelUuid);
        scanFilters.add(builder.build());

        return scanFilters;
    }

    private ScanSettings buildScanSettings() {
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
    }

    private AdvertiseSettings buildAdvertisingSettings() {
        AdvertiseSettings.Builder settings = new AdvertiseSettings.Builder();
        settings.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        settings.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settings.setTimeout(0);
        return settings.build();
    }

    private AdvertiseData buildAdvertiseData() {
        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.addServiceUuid(mParcelUuid);
        return dataBuilder.build();
    }


    private void startAdvertising() {
        Log.d(TAG, "Started Advertising");
        AdvertiseSettings settings = buildAdvertisingSettings();
        AdvertiseData data = buildAdvertiseData();
        AdvertiseCallback callback = new LeAdvertiseCallBack();

        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.startAdvertising(settings, data, callback);
        }
    }

    private void scanLeDevice(final boolean enable) {
        List<ScanFilter> filter = buildScanFilters();
        ScanSettings settings = buildScanSettings();
        ScanCallback callback = new LeScanCallback();


        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothLeScanner.stopScan(callback);
                    Log.d(TAG, "Stopping Scan");

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for(BluetoothDevice device : mBTDevices){
                                mGattClient = device.connectGatt(getApplicationContext(), false, mGattClientCallBack);
                            }

                        }
                    }, DELAY_PERIOD);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothLeScanner.startScan(filter, settings, callback);
            Log.d(TAG, "Starting Scan");


        }

        else {
            mScanning = false;
            mBluetoothLeScanner.stopScan(callback);
            Log.d(TAG, "Stopping Scan");
        }
    }

    private class GattServerCallBack extends BluetoothGattServerCallback{

        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState){
            String string = "";

            switch (newState){
                case BluetoothProfile.STATE_CONNECTED:
                    string = "Connected";
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    string = "Disconnected";
                    break;
            }
            Log.d(TAG, device.toString() +" " + string);
        }
        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestID, BluetoothGattCharacteristic characteristic,
                                                   boolean preparedWrite, boolean responseNeeded, int offset, byte[] value){
            super.onCharacteristicWriteRequest(device,requestID,characteristic,preparedWrite, responseNeeded, offset, value);

            Log.d(TAG, "WriteRequest on server");
            Log.d(TAG, value.toString());
            Toast.makeText(getApplicationContext(), value.toString(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
        }


        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            super.onNotificationSent(device, status);
        }

        @Override
        public void onMtuChanged(BluetoothDevice device, int mtu) {
            super.onMtuChanged(device, mtu);
        }

        @Override
        public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
            super.onExecuteWrite(device, requestId, execute);
        }
    }

    private class GattClientCallBack extends BluetoothGattCallback{
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState){
            super.onConnectionStateChange(gatt, status, newState);
            boolean isSuccess = status == BluetoothGatt.GATT_SUCCESS;
            boolean isConnected = newState == BluetoothProfile.STATE_CONNECTED;
            Log.d(TAG, status + " " + newState);
            Log.d(TAG, "onConnectionStateChange: Client " + gatt  +  " success: " + isSuccess + " connected: " + isConnected);
            if(isSuccess && isConnected){
                gatt.discoverServices();
            }
            else{
                
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.d(TAG, "On services discovered, have gatt: " + gatt);
            if(status == BluetoothGatt.GATT_SUCCESS){
                BluetoothGattService service = gatt.getService(SERVICE_UUID);
                mBluetoothGattCharacteristic = service.getCharacteristic(MESSAGE_UUID);

                Date date = new Date();
                ChatMessage cm = new ChatMessage("Ola",date, "me", "to", "message" );
                sendMessage(cm);
            }
        }
    }
    private class LeAdvertiseCallBack extends AdvertiseCallback {
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            String errorReason = "";
            Log.d(TAG, "Advertising Failed ");
            switch (errorCode) {
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
            Log.d(TAG, "Advertising Failed: " + errorReason);

        }

        @Override
        public void onStartSuccess(AdvertiseSettings settings) {
            super.onStartSuccess(settings);
            Log.d(TAG, "Advertising started Success");
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
            Log.d(TAG, result.toString());
            BluetoothDevice device = result.getDevice();
            if(!mBTDevices.contains(device)){
                mBTDevices.add(device);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d(TAG, "Error scanning " + errorCode);

        }
    }
}

