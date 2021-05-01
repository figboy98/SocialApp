package pt.up.fc.progmovel.socialapp.ui.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import pt.up.fc.progmovel.socialapp.shared.User;
import pt.up.fc.progmovel.socialapp.ui.chat.ChatMessage;

public class PostsViewModel extends ViewModel {

    private final MutableLiveData<Stack<Post>> postList = new MutableLiveData<>();

    public PostsViewModel() {
        Stack<Post> posts = new Stack<>();
        posts.add(new Post(System.currentTimeMillis(), new User("123", "test_user"), "blabla"));

        posts.add(new Post(System.currentTimeMillis(), new User("123", "test_user"), "blabla1"));

        posts.add(new Post(System.currentTimeMillis(), new User("123", "test_user"), "blabla2"));
        postList.setValue(posts);
    }

    public MutableLiveData<Stack<Post>> getPost() {
        return postList;
    }
}