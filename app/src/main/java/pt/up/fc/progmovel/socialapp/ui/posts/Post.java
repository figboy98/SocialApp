package pt.up.fc.progmovel.socialapp.ui.posts;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import pt.up.fc.progmovel.socialapp.database.ChatMessage;


@Entity
public class Post implements Serializable, Comparable<Post>{

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id = UUID.randomUUID().toString();
    private long timestamp;
    private String userId;
    private String content;

    public Post(long timestamp, String userId, String content) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.content = content;
    }

    public Post() {}

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(Post o) {
        return Long.compare(o.timestamp, this.timestamp);
    }

}
