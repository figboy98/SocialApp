package pt.up.fc.progmovel.socialapp.ui.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import pt.up.fc.progmovel.socialapp.database.ChatRepository;
import pt.up.fc.progmovel.socialapp.database.GroupChatWithMessages;

public class ChatMessageListViewModel extends AndroidViewModel {

    private final LiveData<GroupChatWithMessages> mGroupChatWithMessages;


    private ChatRepository mChatRepository;
    private String mChatID;


    public ChatMessageListViewModel(@NonNull Application application, String chatID) {
        super(application);
        mChatID = chatID;
        mChatRepository = new ChatRepository(application);
        mGroupChatWithMessages = mChatRepository.getMessagesOfGroupChat(mChatID);
    }

    public LiveData<GroupChatWithMessages> getMessages() {
        return mGroupChatWithMessages;
    }

    @Override
    public void onCleared(){

    }
}