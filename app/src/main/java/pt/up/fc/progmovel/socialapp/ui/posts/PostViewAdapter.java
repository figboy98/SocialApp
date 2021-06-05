package pt.up.fc.progmovel.socialapp.ui.posts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PostViewAdapter extends RecyclerView.Adapter<PostViewHolder>{

    private List<Post> postsList;
    private Activity activity;

    public PostViewAdapter(List<Post> postStack, Activity activity){
        this.postsList = postStack;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new PostViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}