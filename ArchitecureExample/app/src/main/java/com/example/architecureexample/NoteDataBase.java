package com.example.architecureexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDataBase extends RoomDatabase {
    private static NoteDataBase instance;
    public  abstract NoteDao noteDao();

    public static synchronized NoteDataBase getInstance(Context context)
    {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDataBase.class, "note_table")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
            }
        return instance;
    }
    private static RoomDatabase.Callback roomCallBack= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateNoteAsyncTask(instance).execute();
        }
    };
    private  static class PopulateNoteAsyncTask extends AsyncTask<Void,Void,Void> {
        private NoteDao noteDao;
        private PopulateNoteAsyncTask(NoteDataBase db){
            noteDao = db.noteDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("JeanMichel","tropBien",1));
            noteDao.insert(new Note("JeanMdichel","tropBfdien",2));
            noteDao.insert(new Note("JeanMddichel","tropdfBien",3));
            noteDao.insert(new Note("JeanMicfffhel","tropBfdien",4));

            return null;
        }
    }
}
