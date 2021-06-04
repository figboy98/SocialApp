package pt.up.fc.progmovel.socialapp.util;

import android.Manifest;
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
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;
import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;

public class BluetoothService extends Service {
    private static final String TAG = "BLUETOOTH_SERVICE";
    private static final String TAG_SCAN_ADVERT = "BLUETOOTH_SCAN_ADVERT";
    private static final UUID SERVICE_UUID = UUID.fromString("e526a16e-f365-472a-87e3-a219d75ff262");
    private static final ParcelUuid mParcelUuid = new ParcelUuid(SERVICE_UUID);
    private static final BluetoothAdapter bt = null;
    private static final ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
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
    private SocialAppRepository mRepository;
    private int code_size;
    private final Charset charset = StandardCharsets.UTF_16;

    @Override
    public void onCreate() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mIBinder = new LocalBinder();
        mLeAdvertiseCallBack = new LeAdvertiseCallBack();
        mLeScanCallback = new LeScanCallback();
        mRepository = new SocialAppRepository(getApplication());
        code_size = Constants.BLUETOOTH_TYPE_CHAT_MESSAGE.length;
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
        if(mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "Bluetooth Service is running...");
            final String CHANNEL_ID = "SocialAppNotification";
            startAdvertising();
            scanLeDevice(true);
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }

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


        }
        else {
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



    private class ConnectedThread extends Thread {
        private final BluetoothSocket mSocket;
        private final InputStream mInputStream;
        private final OutputStream mOutputStream;
        private String typeOfMessage;
        private String mGroupId;

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
            int message_type = 0;

            byte[] data = new byte[1024];
            int current = 0;
            int bytes = 0;
            byte[] tmp;
            byte[] code;
            byte[] messageArray;


            while (true) {
                try {
                    bytes = mInputStream.read(data);
                    buffer.write(data, 0, bytes);

                    /*
                    beginning of message, detect type of message to be received
                     */
                    if (current == 0) {
                        tmp = buffer.toByteArray();
                        code = Arrays.copyOfRange(tmp, 0, code_size);

                        if (Arrays.equals(code, Constants.BLUETOOTH_TYPE_CHAT_MESSAGE)) {
                            typeOfMessage = Constants.TYPE_CHAT_MESSAGE;
                        }
                        else if (Arrays.equals(code, Constants.BLUETOOTH_TYPE_GROUP_CHAT_ID_MESSAGE)) {
                            typeOfMessage = Constants.TYPE_GROUP_CHAT_ID;

                        }
                        else if (Arrays.equals(code, Constants.BLUETOOTH_TYPE_POST)) {
                            typeOfMessage = Constants.TYPE_POST_MESSAGE;

                        }
                    }
                    current += bytes;

                    /*
                    Check for end of message code
                     */

                    tmp = buffer.toByteArray();
                    int size = tmp.length;
                    code = Arrays.copyOfRange(tmp, size - code_size, size);

                    if(Arrays.equals(code,Constants.BLUETOOTH_TYPE_END_OF_MESSAGE) && typeOfMessage.equals(Constants.TYPE_GROUP_CHAT_ID)){
                        tmp = buffer.toByteArray();
                        messageArray = Arrays.copyOfRange(tmp, code_size, tmp.length-code_size);
                        mGroupId = new String(messageArray, charset );
                        current =0;
                        buffer.reset();
                    }

                    else if (Arrays.equals(code, Constants.BLUETOOTH_TYPE_END_OF_MESSAGE)) {
                        buffer.flush();
                        tmp = buffer.toByteArray();
                        messageArray = Arrays.copyOfRange(tmp, code_size, tmp.length-code_size);

                        Log.d(TAG, "End of Message, Bytes received in total: " + current);
                        current = 0;
                        buffer.reset();
                        dataReceived(messageArray);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed Reading input buffer " + e);
                    mBTDevices.remove(mSocket.getRemoteDevice());
                    break;
                }
            }
        }

        public void write(byte[] bytes, byte[] typeOfMessage) {
            try {
                byte[] buffer = new byte[bytes.length];
                int bytesSent=0;
                int counter=0;

                Log.d(TAG, "Bytes to send: " + bytes.length);
                //mOutputStream.write(buffer);
                InputStream object = new ByteArrayInputStream(bytes);
                BufferedInputStream out = new BufferedInputStream(object, 1024);

                mOutputStream.write(typeOfMessage);

                while((bytesSent = out.read(buffer))!=-1){
                    mOutputStream.write(buffer,0,bytesSent);
                    counter+=bytesSent;
                }

                mOutputStream.write(Constants.BLUETOOTH_TYPE_END_OF_MESSAGE);
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


    public void write(byte[] bytes, byte[] typeOfMessage) {
        mConnectedThread.write(bytes, typeOfMessage);
    }

    public void verifyWriteReadPermissions() {
        int write = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (write != PackageManager.PERMISSION_GRANTED || read != PackageManager.PERMISSION_GRANTED) {


        }
    }

    public Uri writeToStorage( byte[] data, String type){
       verifyWriteReadPermissions();
       Uri uri = null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "name");
        OutputStream outputStream = null;
        String name = UUID.randomUUID().toString().substring(0,5);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.Q){
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name );
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            uri= resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

            try {
                outputStream = resolver.openOutputStream(Objects.requireNonNull(uri));
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Writing File Failed: " + e);
            }

        }
        else{
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File image = new File(imagesDir, name +".png");
            uri = Uri.fromFile(image);
            try {
                outputStream = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Writing File Failed: " + e);
            }
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    public void chatMessageReceived(ChatMessage message){
        String type = message.getType();

        if(type.equals("image")){
            Log.d(TAG, "Image Received");
            Uri uri = writeToStorage(message.getDataBytes(), "image");
            message.setTextMessage(uri.toString());

        }
        else if(type.equals("video")){
            Log.d(TAG, "Video Received");
        }
        else if(type.equals("text")){
            //mRepository.insertChatMessage(message);
        }
        mRepository.insertChatMessage(message);
    }

    public Object byteToObject(byte[] bytes){
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
        return object;

    }
    public void dataReceived(byte[] bytes){
        Log.d(TAG, "Data Received Function");

        Object object= byteToObject(bytes);

        String type = null;

        if (object != null) {
            type = object.getClass().toString();
        }
        else{
            return;
        }

        if(type.equals("class pt.up.fc.progmovel.socialapp.database.ChatMessage")){
            Log.d(TAG, "ChatMessage Received");
            chatMessageReceived((ChatMessage) object);

        }
        else if(type.equals("String")){
            Log.d(TAG, "String Received");
        }
    }

}

