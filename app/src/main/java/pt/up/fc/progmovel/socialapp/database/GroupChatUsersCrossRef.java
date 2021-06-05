package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(primaryKeys = {"userId", "groupChatID"})
public class GroupChatUsersCrossRef {
    @NonNull
    private final String userId;
    @NonNull
    private final String groupChatID;


    public GroupChatUsersCrossRef(@NotNull String userId, @NotNull String groupChatID){
        this.userId = userId;
        this.groupChatID = groupChatID;
    }

    @NotNull
    public String getGroupChatID() {
        return groupChatID;
    }

    @NotNull
    public String getUserId() {
        return userId;
    }

  /*  public void setGroupChatID(@NotNull String groupID) {
        this.groupChatID = groupID;
    }

    public void setUserId(@NotNull String userId) {
        this.userId = userId;
    }*/
}
