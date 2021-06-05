package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class User {
    @NonNull
    @PrimaryKey()
    private final String userId;
    private String name;

    public User(String name, @NotNull String userId) {
        this.userId = userId;
        this.name = name;
    }

    @NotNull
    public String getUserId(){
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
