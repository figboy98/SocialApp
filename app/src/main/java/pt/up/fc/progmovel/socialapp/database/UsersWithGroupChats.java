package pt.up.fc.progmovel.socialapp.database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import pt.up.fc.progmovel.socialapp.shared.User;

public class UsersWithGroupChats {
    @Embedded User user;
    @Relation(
            parentColumn = "userID",
            entityColumn = "groupChatID",
            associateBy = @Junction(GroupChatUsersCrossRef.class)
    )
    public List<GroupChat> groupChats;
}
