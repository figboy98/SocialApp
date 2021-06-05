package pt.up.fc.progmovel.socialapp.ui.posts;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;

import java.util.Date;
import java.sql.Timestamp;


public class PostViewHolder extends RecyclerView.ViewHolder {

    private Post post;
    private TextView postTextView;
    private TextView postUserView;
    private TextView postDateView;

    public PostViewHolder(LayoutInflater inflater, ViewGroup parent){
        super(inflater.inflate(R.layout.post_text,parent,false));
        postTextView = itemView.findViewById(R.id.post_content);
        postUserView = itemView.findViewById(R.id.post_user);
        postDateView = itemView.findViewById(R.id.post_date);
    }

    public void bind(Post post){
        this.post = post;
        postTextView.setText(post.getContent());
        postDateView.setText(((Date)new Timestamp(post.getTimestamp())).toString());
        postUserView.setText(post.getUserId());
    }

}
