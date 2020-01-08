package com.cleanup.audriclarose.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cleanup.audriclarose.model.Task;

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
