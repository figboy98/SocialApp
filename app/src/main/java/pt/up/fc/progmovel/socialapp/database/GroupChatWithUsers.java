package pt.up.fc.progmovel.socialapp.database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import pt.up.fc.progmovel.socialapp.shared.User;

public class GroupChatWithUsers {
    @Embedded
    GroupChat group;
    @Relation(
            parentColumn = "groupChatID",
            entityColumn = "userID",
            associateBy = @Junction(GroupChatUsersCrossRef.class)
    )
    public List<User> users;
}