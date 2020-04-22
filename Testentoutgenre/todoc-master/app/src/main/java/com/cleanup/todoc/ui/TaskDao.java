package com.cleanup.todoc.ui;

import android.arch.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tache ORDER BY projectId DESC")
    LiveData<List<Task>> selectAllTask();

    @Insert
    void insertTask(Task task);

    @Update
    void UploadTask(Task task);

    @Delete
    void DeleteTask(Task task);

    @Query("DELETE FROM tache")
    void DeleteAllTask();
}
