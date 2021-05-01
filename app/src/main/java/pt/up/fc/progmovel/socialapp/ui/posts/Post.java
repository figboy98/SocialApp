package pt.up.fc.progmovel.socialapp.ui.posts;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import pt.up.fc.progmovel.socialapp.shared.User;

public class Post {

    private final long timestamp;
    private final String id = UUID.randomUUID().toString();
    private final User user;
    private final String content;

    public Post(long timestamp, User user, String content) {
        this.timestamp = timestamp;
        this.user = user;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }
}
