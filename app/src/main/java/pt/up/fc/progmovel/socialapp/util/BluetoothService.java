package pt.up.fc.progmovel.socialapp.util;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;

public class BluetoothService extends Service {
    private static final String TAG = "BLUETOOTH_SERVICE";
    private static final String TAG_SCAN_ADVERT = "BLUETOOTH_SCAN_ADVERT";
    private static final UUID SERVICE_UUID = UUID.fromString("e526a16e-f365-472a-87e3-a219d75ff262");
    private static final ParcelUuid mParcelUuid = new ParcelUuid(SERVICE_UUID);
    private static final BluetoothAdapter bt = null;
    private static final ArrayList<BluetoothDevice> mBTDevices = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private BluetoothServerSocket mServerSocket;
    private BluetoothDevice mBluetoothDevice;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private LeAdvertiseCallBack mLeAdvertiseCallBack;
    private LeScanCallback mLeScanCallback;
    private IBinder mIBinder;


    @Override
    public void onCreate() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mIBinder = new LocalBinder();
        mLeAdvertiseCallBack = new LeAdvertiseCallBack();
        mLeScanCallback = new LeScanCallback();
    }

    public class LocalBinder extends Binder {
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Bluetooth Service is running...");
        final String CHANNEL_ID = "SocialAppNotification";
        startAdvertising();
        scanLeDevice(true);
        mAcceptThread = new AcceptThread();
        mAcceptThread.start();
        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Bluetooth Service is stopping...");
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
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .build();
    }

    private AdvertiseSettings buildAdvertisingSettings() {
        AdvertiseSettings.Builder settings = new AdvertiseSettings.Builder();
        settings.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        settings.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM);
        settings.setTimeout(0);
        return settings.build();
    }

    private AdvertiseData buildAdvertiseData() {
        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.addServiceUuid(mParcelUuid);
        return dataBuilder.build();
    }


    private void startAdvertising() {
        Log.d(TAG_SCAN_ADVERT, "Started Advertising");
        AdvertiseSettings settings = buildAdvertisingSettings();
        AdvertiseData data = buildAdvertiseData();

        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.startAdvertising(settings, data, mLeAdvertiseCallBack);
        }
    }

    private void makeBluetoothConnection(BluetoothDevice device) {
        startClient(device);
    }

    private void scanLeDevice(boolean enable) {
        List<ScanFilter> filter = buildScanFilters();
        ScanSettings settings = buildScanSettings();


        if (enable) {
            mBluetoothLeScanner.startScan(filter, settings, mLeScanCallback);
            Log.d(TAG_SCAN_ADVERT, "Starting Scan");


        } else {
            mBluetoothLeScanner.stopScan(mLeScanCallback);
            Log.d(TAG_SCAN_ADVERT, "Stopping Scan");
        }
    }

    private static class LeAdvertiseCallBack extends AdvertiseCallback {
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            String errorReason = "";
            Log.d(TAG_SCAN_ADVERT, "Advertising Failed ");
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
            Log.d(TAG_SCAN_ADVERT, "Advertising Failed: " + errorReason);

        }

        @Override
        public void onStartSuccess(AdvertiseSettings settings) {
            super.onStartSuccess(settings);
            Log.d(TAG_SCAN_ADVERT, "Advertising started Success");
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
            Log.d(TAG_SCAN_ADVERT, result.toString());
            BluetoothDevice device = result.getDevice();
            if (!mBTDevices.contains(device)) {
                mBTDevices.add(device);
                makeBluetoothConnection(device);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d(TAG_SCAN_ADVERT, "Error scanning " + errorCode);

        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("SocialApp Server Communication", SERVICE_UUID);
            } catch (IOException e) {
                Log.d(TAG, "Listen failed " + e);
            }
            mServerSocket = tmp;
            Log.d(TAG, "Accept Thread: " + mServerSocket);
        }

        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = mServerSocket.accept();
                    Log.d(TAG, "Server socket accepted connection " + socket);
                } catch (IOException e) {
                    Log.d(TAG, "Server socket Accept failed " + e);
                    break;
                }
                if (socket != null) {
                    connected(socket);
                }
            }

        }

        public void cancel() {
            try {
                mServerSocket.close();
                Log.d(TAG, "Server socket close connection");
            } catch (IOException e) {
                Log.d(TAG, "Server socket close connection failed " + e);
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mSocket;
        private  final BluetoothDevice mDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mDevice = device;

            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(SERVICE_UUID);
                Log.d(TAG, "ConnectThread creating connection");

            } catch (IOException e) {
                Log.d(TAG, "ConnectThread creating connection failed " + e);
            }
            mSocket = tmp;
        }

        public void run() {
            try {
                mSocket.connect();
            } catch (IOException e) {
                Log.d(TAG, "ConnectThread  connecting failed " + e);
                try {
                    mSocket.close();
                    Log.d(TAG, "ConnectThread, closing connection after failed atempt");
                } catch (IOException e2) {
                    Log.d(TAG, "ConnectThread, error closing connection after failed atempt " + e2);
                }
                return;
            }
            connected(mSocket);
        }

        public void cancel() {
            try {
                mSocket.close();
                Log.d(TAG, "Closing ConnectThread socket");
            } catch (IOException e) {
                Log.d(TAG, "Closing ConnectThread socket failed " + e);
            }
        }
    }

    public void startClient(BluetoothDevice device) {
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }


    public void dataReceived(byte[] bytes){
        Log.d(TAG, "Data Received Function");

        Object object =null;
        ObjectInput obIn=null;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            obIn = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            Log.d(TAG, "Data Received: Creating object failed: " + e);
        }

        try {
            try {
                if(obIn !=null){
                    object =  obIn.readObject();
                }
                else{
                    Log.d(TAG, "Unable to open data stream");
                }
            } catch (IOException e) {
                Log.d(TAG, "Data Received: Reading object failed: " + e);

            }
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "Data Received: Object class not found: " + e);

        }
        String type = null;

        if (object != null) {
            type = object.getClass().toString();
            Log.d(TAG, type);
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mSocket;
        private final InputStream mInputStream;
        private final OutputStream mOutputStream;

        public ConnectedThread(BluetoothSocket socket) {
            mSocket = socket;

            Log.d(TAG, "Connected thread with device: " + socket.getRemoteDevice().getAddress());
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
                Log.d(TAG, "Creating input/output streams");
            } catch (IOException e) {
                Log.d(TAG, "Creating input/output streams failed " + e);
            }
            mInputStream = tmpIn;
            mOutputStream = tmpOut;

        }

        public void run() {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] data = new byte[20];
            int current=0;
            int bytes=0;
            int tmp=0;

            while (true) {
                try {
                    bytes = mInputStream.read(data);
                    buffer.write(data,0, bytes);
                    current+= bytes;

                    if(bytes < data.length){
                        buffer.flush();
                        byte[] byteArray = buffer.toByteArray();
                        Log.d(TAG,"Bytes received total: " + current);
                        current=0;
                        //Function to take care of the received data
                        dataReceived(byteArray);
                   }

                } catch (IOException e) {
                    Log.d(TAG, "Failed Reading input buffer " + e);
                    mBTDevices.remove(mSocket.getRemoteDevice());
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                byte[] buffer = new byte[bytes.length];
                int bytesSent=0;
                int counter=0;

                Log.d(TAG, "Bytes to send: " + bytes.length);
                //mOutputStream.write(buffer);
                InputStream object = new ByteArrayInputStream(bytes);
                BufferedInputStream out = new BufferedInputStream(object, 1024);

                while((bytesSent = out.read(buffer))!=-1){
                    mOutputStream.write(buffer,0,bytesSent);
                    counter+=bytesSent;
                }
                Log.d(TAG, "Total bytes sent: " + counter);

                } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

        public void cancel() {
            try {
                mSocket.close();
                Log.d(TAG, "Closing Connected Thread");
            } catch (IOException e) {
                Log.d(TAG, "Closing Connected Thread Failed " + e);
            }
        }
    }

    private void connected(BluetoothSocket socket) {
        Log.d(TAG, "Connected function");

        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
    }


    public void write(byte[] bytes) {
        mConnectedThread.write(bytes);
    }

}

