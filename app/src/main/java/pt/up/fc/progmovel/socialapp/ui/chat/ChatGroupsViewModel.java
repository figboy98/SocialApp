package pt.up.fc.progmovel.socialapp.ui.chat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import pt.up.fc.progmovel.socialapp.database.ChatRepository;
import pt.up.fc.progmovel.socialapp.database.UsersWithGroupChats;

public class ChatGroupsViewModel extends AndroidViewModel {

    private final LiveData<UsersWithGroupChats> mChatGroups;

    public ChatGroupsViewModel(@NonNull Application application, String userID) {
        super(application);
        ChatRepository chatRepository = new ChatRepository(application);
        mChatGroups = chatRepository.getGroupsFromUser(userID);
    }
    public LiveData<UsersWithGroupChats> getChatGroups(){

        return mChatGroups;
    }

    @Override
    public void onCleared(){

    }
}
