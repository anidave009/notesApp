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

    // ExecutorService runs database operations on a background thread
    // this is what replaces allowMainThreadQueries()
    // 4 means a pool of 4 threads available for background work
    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public NoteRepository(Application application) {
        // get the database instance and DAO
        NoteDatabase db = NoteDatabase.getInstance(application);
        noteDao = db.noteDao();
        // fetch all notes once — LiveData keeps this updated automatically
        allNotes = noteDao.getAllNotes();
    }

    // LiveData is returned directly — no background thread needed for reads
    // Room handles this automatically when you return LiveData from DAO
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    // returns live filtered list for a specific folder
    public LiveData<List<Note>> getNotesByFolder(int folderId) {
        return noteDao.getNotesByFolder(folderId);
    }

    // insert runs on background thread via executor
    public void insert(Note note) {
        executor.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        executor.execute(() -> noteDao.update(note));
    }

    public void delete(Note note) {
        executor.execute(() -> noteDao.delete(note));
    }
}