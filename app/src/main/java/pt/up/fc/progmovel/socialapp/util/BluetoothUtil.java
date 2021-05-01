package pt.up.fc.progmovel.socialapp.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public final class BluetoothUtil {

    private BluetoothUtil(){}

    public static void requestBluetoothEnabled(Activity activity){
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivity(enableBtIntent);
        }
    }

}
