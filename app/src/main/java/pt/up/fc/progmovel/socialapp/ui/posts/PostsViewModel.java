package pt.up.fc.progmovel.socialapp.ui.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostsViewModel extends ViewModel {

    private final List<Post> posts = new ArrayList<>();

    public PostsViewModel() {

    }

    public List<Post> getPosts() {
        return posts;
    }

}