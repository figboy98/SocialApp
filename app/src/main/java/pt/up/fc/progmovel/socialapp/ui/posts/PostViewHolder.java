package pt.up.fc.progmovel.socialapp.ui.posts;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;

import pt.up.fc.progmovel.socialapp.ui.posts.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {

    private Post post;
    private TextView postTextView;

    public PostViewHolder(LayoutInflater inflater, ViewGroup parent){
        super(inflater.inflate(R.layout.post_text,parent,false));
        postTextView = itemView.findViewById(R.id.post_content);
    }

    public void bind(Post post){
        this.post = post;
        postTextView.setText(post.getContent());
    }

}
