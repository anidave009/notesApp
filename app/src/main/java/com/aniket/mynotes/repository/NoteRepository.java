package com.aniket.mynotes.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.aniket.mynotes.db.NoteDao;
import com.aniket.mynotes.db.NoteDatabase;
import com.aniket.mynotes.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public NoteRepository(Application application) {
        // get the database instance and DAO
        NoteDatabase db = NoteDatabase.getInstance(application);
        noteDao = db.noteDao();
        // fetch all notes once — LiveData keeps this updated automatically
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        executor.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        executor.execute(() -> noteDao.update(note));
    }

    public void delete(Note note) {
        executor.execute(() -> noteDao.delete(note));
    }
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}