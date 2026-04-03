package com.aniket.mynotes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aniket.mynotes.model.Note;
import com.aniket.mynotes.repository.NoteRepository;

import java.util.List;

public class NoteVM extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allnotes;
    public NoteVM(@NonNull Application application){
        super(application);
        repository=new NoteRepository(application);
        allnotes=repository.getAllNotes();
    }

}
