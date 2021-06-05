package pt.up.fc.progmovel.socialapp.ui.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class ChatGroupsViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final String mUserId;

    public ChatGroupsViewModelFactory(Application application, String userID){
        mApplication = application;
        mUserId = userID;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ChatGroupsViewModel.class)){
            return (T)new ChatGroupsViewModel(mApplication, mUserId);
        }
        throw new IllegalArgumentException("ChatGroupViewModel not found");

    }
}