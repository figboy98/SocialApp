package pt.up.fc.progmovel.socialapp.ui.posts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pt.up.fc.progmovel.socialapp.database.Post;
import pt.up.fc.progmovel.socialapp.database.SocialAppRepository;

public class PostsViewModel extends AndroidViewModel {

    private LiveData<List<Post>> postList;

    public PostsViewModel(@NonNull @NotNull Application application) {
        super(application);
        SocialAppRepository mSocialAppRepository = new SocialAppRepository(application);
        postList = mSocialAppRepository.getPosts();
    }

    public LiveData<List<Post>> getPost() {
        return postList;
    }

}