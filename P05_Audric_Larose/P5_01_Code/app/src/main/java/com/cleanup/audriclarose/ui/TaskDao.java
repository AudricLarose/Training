package com.cleanup.audriclarose.ui;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.audriclarose.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM bdd ORDER BY Id ASC")
    LiveData<List<Task>> selectAllTask();

    @Insert
    void insertTask(Task task);

    @Update
    void UploadTask(Task task);

    @Delete
    void DeleteTask(Task task);

    @Query("DELETE FROM bdd")
    void DeleteAllTask();
}
