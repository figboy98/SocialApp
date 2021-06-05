package pt.up.fc.progmovel.socialapp.ui.posts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import pt.up.fc.progmovel.socialapp.ui.chat.ChatGroupsViewModel;

public class PostsViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;

    public PostsViewModelFactory(Application application){
        mApplication = application;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(PostsViewModel.class)){
            return (T)new PostsViewModel(mApplication);
        }

        throw new IllegalArgumentException("PostsViewModel not found");
    }
}