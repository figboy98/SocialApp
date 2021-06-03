package pt.up.fc.progmovel.socialapp.ui.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.database.UsersWithGroupChats;

public class ChatGroupsViewModel extends AndroidViewModel {

    private final LiveData<UsersWithGroupChats> mChatGroups;

    public ChatGroupsViewModel(@NonNull Application application, String userID) {
        super(application);
        SocialAppRepository socialAppRepository = new SocialAppRepository(application);
        mChatGroups = socialAppRepository.getGroupsFromUser(userID);
    }
    public LiveData<UsersWithGroupChats> getChatGroups(){

        return mChatGroups;
    }

    @Override
    public void onCleared(){

    }
}
