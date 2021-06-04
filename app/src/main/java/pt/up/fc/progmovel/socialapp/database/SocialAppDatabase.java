package pt.up.fc.progmovel.socialapp.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Database(
        entities = {
                ChatMessage.class,
                GroupChat.class,
                User.class,
                GroupChatUsersCrossRef.class,
                GroupChatMessagesCrossRef.class,
        },
        exportSchema = false,
        version =1
)
public  abstract class SocialAppDatabase extends RoomDatabase {

    public abstract DatabaseDao chatDao();

    volatile private static SocialAppDatabase db = null;

    public static synchronized SocialAppDatabase getDatabase(Context context){
            if(db == null){
                db = Room.databaseBuilder(context.getApplicationContext(),
                        SocialAppDatabase.class, "SocialApp-database")
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
        private DatabaseDao mDatabaseDao;
        private  PopulateDbAsyncTask(SocialAppDatabase db){
            mDatabaseDao = db.chatDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            String id1 = UUID.fromString("7b75907c-c4b9-11eb-8529-0242ac130003").toString();
            String id2 = UUID.fromString("7b759450-c4b9-11eb-8529-0242ac130003").toString();
            User user1 = new User("123","Fabio", id1);
            User user2 = new User("124","Vasco",id2);
            mDatabaseDao.insertUser(user1);
            mDatabaseDao.insertUser(user2);
            String cId1 = UUID.fromString("94eacd18-c524-11eb-8529-0242ac130003").toString();
            String cId2 = UUID.fromString("94eacf8e-c524-11eb-8529-0242ac130003").toString();

           GroupChat chat = new GroupChat("Grupo 1",cId1);
           GroupChat chat2 = new GroupChat("Grupo 2",cId2);
            mDatabaseDao.insertGroupChat(chat);
            mDatabaseDao.insertGroupChat(chat2);

            GroupChatUsersCrossRef  g1 = new GroupChatUsersCrossRef(user1.getUserID(), chat.getGroupChatID());
            GroupChatUsersCrossRef  g2 = new GroupChatUsersCrossRef(user2.getUserID(), chat.getGroupChatID());
            GroupChatUsersCrossRef  g3 = new GroupChatUsersCrossRef(user1.getUserID(), chat2.getGroupChatID());
            GroupChatUsersCrossRef  g4 = new GroupChatUsersCrossRef(user2.getUserID(), chat2.getGroupChatID());
            mDatabaseDao.insertGroupChatUsersCrossRef(g1);
            mDatabaseDao.insertGroupChatUsersCrossRef(g2);
            mDatabaseDao.insertGroupChatUsersCrossRef(g3);
            mDatabaseDao.insertGroupChatUsersCrossRef(g4);
            return null;
        }
    }
}
