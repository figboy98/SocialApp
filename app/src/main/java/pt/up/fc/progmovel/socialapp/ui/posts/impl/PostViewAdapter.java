package pt.up.fc.progmovel.socialapp.ui.posts.impl;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Stack;

import pt.up.fc.progmovel.socialapp.ui.posts.Post;


public class PostViewAdapter extends RecyclerView.Adapter{

    private Stack<Post> postStack;
    private Activity activity;

    public PostViewAdapter(Stack<Post> postStack, Activity activity){
        this.postStack = postStack;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new PostViewHolder(layoutInflater, parent);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = postStack.get(position);
        ((PostViewHolder) holder).bind(post);
    }

    @Override
    public int getItemCount() {
        return postStack.size();
    }
}