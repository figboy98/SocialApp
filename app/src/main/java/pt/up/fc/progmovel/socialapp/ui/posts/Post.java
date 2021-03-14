package pt.up.fc.progmovel.socialapp.ui.posts;

import java.util.Date;

import pt.up.fc.progmovel.socialapp.shared.User;

public class Post {

    private final Date timestamp;
    private final int id;
    private final User user;
    private final String content;

    public Post(Date timestamp, int id, User user, String content) {
        this.timestamp = timestamp;
        this.id = id;
        this.user = user;
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }
}
