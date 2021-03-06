package pt.up.fc.progmovel.socialapp.ui.posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;

import java.util.List;

import pt.up.fc.progmovel.socialapp.database.Post;
import pt.up.fc.progmovel.socialapp.ui.posts.input.PostsInput;


public class PostsFragment extends Fragment {

    private PostsViewModel postsViewModel;
    private RecyclerView postsRecyclerView;
    private PostViewAdapter postViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        PostsViewModelFactory factory = new PostsViewModelFactory(requireActivity().getApplication());
        postsViewModel = new ViewModelProvider(requireActivity(), factory).get(PostsViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        Button btn = view.findViewById(R.id.new_post_button);

        btn.setOnClickListener(i -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), PostsInput.class);
            startActivity(intent);
        });

        postsRecyclerView = view.findViewById(R.id.posts_list);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Observer<List<Post>> postsObserver = posts -> {
            if(posts != null){
                updateUI(posts);
            }
        };

        postsViewModel.getPost().observe(getViewLifecycleOwner(), postsObserver);
        return view;
    }

    private void updateUI(List<Post> posts){
        if(postViewAdapter == null){
            postViewAdapter = new PostViewAdapter(posts, getActivity());
            postsRecyclerView.setAdapter(postViewAdapter);
        }else{
            postViewAdapter.notifyDataSetChanged();
        }

        postViewAdapter.setPostsList(posts);
    }

}