package pt.up.fc.progmovel.socialapp.ui.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.database.GroupChatWithMessages;

public class ChatMessageListViewModel extends AndroidViewModel {

    private final LiveData<GroupChatWithMessages> mGroupChatWithMessages;


    public ChatMessageListViewModel(@NonNull Application application, String chatID) {
        super(application);
        SocialAppRepository mSocialAppRepository = new SocialAppRepository(application);
        mGroupChatWithMessages = mSocialAppRepository.getMessagesOfGroupChat(chatID);
    }

    public LiveData<GroupChatWithMessages> getMessages() {
        return mGroupChatWithMessages;
    }

    @Override
    public void onCleared(){

    }
}