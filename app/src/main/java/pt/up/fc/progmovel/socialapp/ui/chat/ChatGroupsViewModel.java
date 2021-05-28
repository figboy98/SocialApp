package pt.up.fc.progmovel.socialapp.ui.chat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pt.up.fc.progmovel.socialapp.database.ChatRepository;
import pt.up.fc.progmovel.socialapp.database.UsersWithGroupChats;

public class ChatGroupsViewModel extends AndroidViewModel {
    private String user2 = "ea63f905-ee65-4df0-9d15-530be680c6f4";
    private String user1 = "484fcf9a-c83d-4719-b6ce-5ac642d784c3";
    private ChatRepository mChatRepository;
    private final LiveData<UsersWithGroupChats> mChatGroups;

    public ChatGroupsViewModel(@NonNull Application application, String UserID) {
        super(application);
        mChatRepository = new ChatRepository(application);
        mChatGroups = mChatRepository.getGroupsFromUser(user1);
        Log.d("sad","sa");

    }
    public LiveData<UsersWithGroupChats> getChatGroups(){
        return mChatGroups;
    }

    @Override
    public void onCleared(){

    }
}
