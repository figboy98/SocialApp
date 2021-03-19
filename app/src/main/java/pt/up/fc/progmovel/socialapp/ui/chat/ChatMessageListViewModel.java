package pt.up.fc.progmovel.socialapp.ui.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatMessageListViewModel extends ViewModel {

    private final MutableLiveData<List<ChatMessage>> mMessageList;

    public ChatMessageListViewModel() {
        mMessageList = new MutableLiveData<List<ChatMessage>>();
        List<ChatMessage> mList = new ArrayList<ChatMessage>();
        Date date = new Date();
        ChatMessage mSent = new ChatMessage("Hello", date, "me", "you");
        ChatMessage mReceived = new ChatMessage("Hello you too", date, "you", "me");
        mList.add(mSent);
        mList.add(mReceived);
        mMessageList.setValue(mList);
    }

    public LiveData<List<ChatMessage>> getMessages() {
        return mMessageList;
    }

    @Override
    public void onCleared(){

    }
}