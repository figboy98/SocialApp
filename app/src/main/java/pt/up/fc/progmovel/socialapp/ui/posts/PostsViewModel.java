package pt.up.fc.progmovel.socialapp.ui.posts;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Stack;
import java.util.UUID;

import pt.up.fc.progmovel.socialapp.database.User;

public class PostsViewModel extends ViewModel {

    private final MutableLiveData<Stack<Post>> postList = new MutableLiveData<>();

    public PostsViewModel() {
        Stack<Post> posts = new Stack<>();
        posts.add(new Post(System.currentTimeMillis(), "7b759450-c4b9-11eb-8529-0242ac130003", "blabla"));
        postList.setValue(posts);
    }

    public MutableLiveData<Stack<Post>> getPost() {
        return postList;
    }
}