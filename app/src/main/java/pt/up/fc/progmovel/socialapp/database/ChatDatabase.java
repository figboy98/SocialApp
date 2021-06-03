package pt.up.fc.progmovel.socialapp.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

@Database(
        entities = {
                ChatMessage.class,
                GroupChat.class,
                User.class,
                GroupChatUsersCrossRef.class,
                GroupChatMessagesCrossRef.class,
        },
        exportSchema = false,
        version =200
)
public  abstract class ChatDatabase extends RoomDatabase {

    public abstract ChatDao chatDao();

    volatile private static ChatDatabase db = null;

    public static synchronized ChatDatabase getDatabase(Context context){
            if(db == null){
                db = Room.databaseBuilder(context.getApplicationContext(),
                        ChatDatabase.class, "chat-database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
            return db;
        }
        private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
            @Override
            public void onCreate(@NonNull @NotNull SupportSQLiteDatabase database) {
                super.onCreate(database);
                new PopulateDbAsyncTask(db).execute();
            }

            @Override
            public void onDestructiveMigration(@NonNull @NotNull SupportSQLiteDatabase database) {
                super.onDestructiveMigration(database);
                new PopulateDbAsyncTask(db).execute();
            }
        };

    private static  class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private ChatDao mChatDao;
        private  PopulateDbAsyncTask(ChatDatabase db){
            mChatDao = db.chatDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            User user1 = new User("123","Jorge Reis");
            User user2 = new User("124","Andreia");
            mChatDao.insertUser(user1);
            mChatDao.insertUser(user2);

           GroupChat chat = new GroupChat("Grupo 1");
           GroupChat chat2 = new GroupChat("Grupo 2");
            mChatDao.insertGroupChat(chat);
            mChatDao.insertGroupChat(chat2);

            GroupChatUsersCrossRef  g1 = new GroupChatUsersCrossRef(user1.getUserID(), chat.getGroupChatID());
            GroupChatUsersCrossRef  g2 = new GroupChatUsersCrossRef(user2.getUserID(), chat.getGroupChatID());
            GroupChatUsersCrossRef  g3 = new GroupChatUsersCrossRef(user1.getUserID(), chat2.getGroupChatID());
            GroupChatUsersCrossRef  g4 = new GroupChatUsersCrossRef(user2.getUserID(), chat2.getGroupChatID());
            mChatDao.insertGroupChatUsersCrossRef(g1);
            mChatDao.insertGroupChatUsersCrossRef(g2);
            mChatDao.insertGroupChatUsersCrossRef(g3);
            mChatDao.insertGroupChatUsersCrossRef(g4);
            return null;
        }
    }
}
