package com.cleanup.todoc.ui;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDataBase extends RoomDatabase {
private static TaskDataBase instance;
public abstract TaskDao taskDao();

public static synchronized TaskDataBase getInstance (Context context) {
    if (instance == null) {
        instance = Room.databaseBuilder(context.getApplicationContext(),
                TaskDataBase.class, "task_database")
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
        }
    };
    private  static class PopulateNoteAsyncTask extends AsyncTask<Void,Void,Void> {
        private TaskDao taskDao;
        private PopulateNoteAsyncTask(TaskDataBase db){
            taskDao = db.taskDao();
        }

        public PopulateNoteAsyncTask(TaskDao taskDao) {this.taskDao = taskDao; }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.insertTask(new Task(1, 3, "truc cool",1222));
            return null;
        }
    }
}

