package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.socialapp.R;

import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.ui.home.HomeViewModel;
import pt.up.fc.progmovel.socialapp.util.BluetoothService;
import pt.up.fc.progmovel.socialapp.util.Constants;

public class ChatActivity extends FragmentActivity {
    private static final String EXTRA_CHAT_ID =  "pt.up.fc.progmovel.socialapp.extra.CHAT_ID";
    private String mChatID;
    private HomeViewModel homeViewModel;
    private BluetoothService mBluetoothService;
    private Boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        mChatID = intent.getStringExtra(EXTRA_CHAT_ID);

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CHAT_ID, mChatID);

//        ServiceConnection connection = new ServiceConnection() {
//
//            @Override
//            public void onServiceConnected(ComponentName className,
//                                           IBinder service) {
//                BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
//                mBluetoothService = binder.getService();
//                mBound = true;
//                byte[] chatId = mChatID.getBytes(mConstants.charset);
//
//                mBluetoothService.write(chatId,mConstants.BLUETOOTH_TYPE_GROUP_CHAT_ID_MESSAGE);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName arg0) {
//
//            }
//        };

//        Intent bluetoohService = new Intent(this, BluetoothService.class);
//        bindService(bluetoohService, connection, Context.BIND_AUTO_CREATE);

        FragmentManager fm = getSupportFragmentManager();

        Fragment inputChat = new ChatInputFragment();
        inputChat.setArguments(bundle);

        Fragment messagesList = new ChatMessageListFragment();
        messagesList.setArguments(bundle);

        fm.beginTransaction()
                    .replace(R.id.chat_message_input_placeholder, inputChat)
                    .replace(R.id.chat_messages_list_placeholder, messagesList)
                    .commit();

        }

}
