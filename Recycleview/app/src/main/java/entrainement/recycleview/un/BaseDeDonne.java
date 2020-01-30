package entrainement.recycleview.un;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ExempleItem.class}, version =1)
public abstract class BaseDeDonne extends RoomDatabase {  // nom class simple et non deja referencé
    private static BaseDeDonne instance;
    public abstract InterfaceDao interfaceDao();

    public static synchronized BaseDeDonne getInstance (Context context) {
        if (instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    BaseDeDonne.class,"training8")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new requete(instance).execute();
        }
    };
    private static class requete extends AsyncTask<Void,Void,Void>{
        private InterfaceDao interfaceDao;
        private requete(BaseDeDonne db) { interfaceDao = db.interfaceDao();}
        @Override
        protected Void doInBackground(Void... voids) {
            interfaceDao.insert(new ExempleItem("note",1));
            return null;
        }
    }

}
