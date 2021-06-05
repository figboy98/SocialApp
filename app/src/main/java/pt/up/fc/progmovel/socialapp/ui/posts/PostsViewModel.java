package pt.up.fc.progmovel.socialapp.ui.posts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;
import pt.up.fc.progmovel.socialapp.database.User;

public class PostsViewModel extends AndroidViewModel {

    private LiveData<List<Post>> postList;

    public PostsViewModel(@NonNull @NotNull Application application) {
        super(application);
        SocialAppRepository mSocialAppRepository = new SocialAppRepository(application);
        postList = mSocialAppRepository.getPosts();
        Log.d("1","");
    }

    public LiveData<List<Post>> getPost() {
        return postList;
    }

}