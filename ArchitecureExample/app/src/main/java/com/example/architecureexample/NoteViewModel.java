package com.example.architecureexample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private LiveData<List<Note>> allNotes;
    private NoteRepository repository;


    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }
    public void insert(Note note){
        repository.insert(note);
    }
    public void update(Note note){
        repository.update(note);
    }
    public void delete(Note note){
        repository.delete(note);
    }
    public void deleall(){
        repository.deleteAllNotes();
    }
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
}