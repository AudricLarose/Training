package com.cleanup.todoc.ui;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.cleanup.todoc.model.Task;

import java.util.List;

public class RepositoryTask {
    private TaskDao taskDao;
    private LiveData<List<Task>> allData;

    public RepositoryTask(Application application){
        TaskDataBase taskDataBase= TaskDataBase.getInstance(application);
        taskDao=taskDataBase.taskDao();
        allData=taskDao.selectAllTask();
    }

    public void InsertData(Task task) {
        new InsertAsynchrone(taskDao).execute(task);

    }
    public void deleteData(Task task) {
        new DeleteAsynchrone(taskDao).execute(task);

    }
    public void uploadData(Task task) {
        new uploadAsynchrone(taskDao).execute(task);
    }
    public void DeleteAllData() {
        new deleteAllAsynchrone(taskDao).execute();
    }

    public LiveData<List<Task>> selectData (){
        return allData;
    }

    public static class InsertAsynchrone extends AsyncTask<Task,Void, Void>{
        private TaskDao taskDao ;

        public InsertAsynchrone(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insertTask(tasks[0]);
            return null;
        }
    }
    public static class DeleteAsynchrone extends AsyncTask<Task,Void, Void>{
        private TaskDao taskDao ;

        public DeleteAsynchrone(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.DeleteTask(tasks[0]);
            return null;
        }
    }
    public static class uploadAsynchrone extends AsyncTask<Task,Void, Void>{
        private TaskDao taskDao ;

        public uploadAsynchrone(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.UploadTask(tasks[0]);
            return null;
        }
    }
    public static class deleteAllAsynchrone extends AsyncTask<Void,Void, Void>{
        private TaskDao taskDao ;

        public deleteAllAsynchrone(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.DeleteAllTask();
            return null;
        }
    }
}
