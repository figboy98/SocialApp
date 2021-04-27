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
        for(int i=0; i<100; i++){
            String f;
            String t;
            if(i%2==0){
                f = "me";
                t=  "you";
            }
            else{
                f="you";
                t="me";
            }

            ChatMessage mSent = new ChatMessage("Hellodssssssssssssssssfdsfsdfsffwefefdsfsfsdfsdfsfs sdfsdfdsfsdfsd dsfsddsfsdf " + i, date, f, t);
            mList.add(mSent);
        }
        mMessageList.setValue(mList);

    }

    public LiveData<List<ChatMessage>> getMessages() {
        return mMessageList;
    }

    @Override
    public void onCleared(){

    }
}