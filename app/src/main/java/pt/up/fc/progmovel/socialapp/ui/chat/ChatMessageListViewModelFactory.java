package pt.up.fc.progmovel.socialapp.ui.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class ChatMessageListViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final String mChatID;

    public ChatMessageListViewModelFactory(Application application, String chatID){
        mApplication = application;
        mChatID = chatID;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ChatMessageListViewModel.class)){

            return (T) new ChatMessageListViewModel(mApplication, mChatID);
        }
        throw new IllegalArgumentException("ChatMessageListViewModel not found");

    }
}
