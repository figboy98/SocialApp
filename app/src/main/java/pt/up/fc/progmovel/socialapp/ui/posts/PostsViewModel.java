package pt.up.fc.progmovel.socialapp.ui.posts;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Stack;

import pt.up.fc.progmovel.socialapp.database.User;

public class PostsViewModel extends ViewModel {

    private final MutableLiveData<Stack<Post>> postList = new MutableLiveData<>();

    public PostsViewModel() {
        Stack<Post> posts = new Stack<>();
        /*posts.add(new Post(System.currentTimeMillis(), new User("123", "test_user"), "blabla"));

        posts.add(new Post(System.currentTimeMillis(), new User("123", "test_user"), "blabla1"));

        posts.add(new Post(System.currentTimeMillis(), new User("123", "test_user"), "blabla2"));*/
        postList.setValue(posts);
    }

    public MutableLiveData<Stack<Post>> getPost() {
        return postList;
    }
}