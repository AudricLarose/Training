package com.cleanup.todoc.ui;

import android.app.Application;

import android.support.annotation.NonNull;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private LiveData<List<Task>> allNotes;
    private RepositoryTask repositoryTask;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repositoryTask= new RepositoryTask(application);
        allNotes=repositoryTask.selectData();
    }

    public void InsertThisData (Task task){
        repositoryTask.InsertData(task);
    }
    public void UploadThisData (Task task){
        repositoryTask.uploadData(task);
    }
    public void deleteThisData (Task task){
        repositoryTask.deleteData(task);
    }
    public void deleteAlldata (){
        repositoryTask.DeleteAllData();
    }
    public LiveData<List<Task>> SelectAllThosedatas (){
        return allNotes;
    }

}
