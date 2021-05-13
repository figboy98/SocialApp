package pt.up.fc.progmovel.socialapp.ui.chat;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                ChatMessage.class,
                GroupChat.class,
                GroupChatUsersCrossRef.class,
                GroupChatMessagesCrossRef.class,
        },
        version = 3
)
public  abstract class ChatDatabase extends RoomDatabase {

    public abstract ChatDao chatDao();

    volatile private static ChatDatabase db = null;

    public static synchronized ChatDatabase getDatabase(Context context){
            if(db == null){
                db = Room.databaseBuilder(context.getApplicationContext(),
                        ChatDatabase.class, "chat-database")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return db;
        }


}
