package com.aniket.mynotes.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aniket.mynotes.model.Note;
import com.aniket.mynotes.repository.NoteRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        // create repository — ViewModel is the only class that touches Repository
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    // Activity observes this — whenever data changes, UI auto-updates
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    // called by FolderDetailActivity — returns only notes in that folder
    public LiveData<List<Note>> getNotesByFolder(int folderId) {
        return repository.getNotesByFolder(folderId);
    }

    // insert without folder — used by MainActivity flow
    public void insert(String title, String content) {
        insert(title, content, null);
    }

    // all business logic lives here — not in Activity

    // insert with folder — used by FolderDetailActivity flow
    public void insert(String title, String content, Integer folderId) {
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(new Date());
        Note note = new Note(title, content, date);
        note.folderId = folderId; // null if called from main flow
        repository.insert(note);
    }

    public void update(int id, String title, String content,Integer folderId) {
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(new Date());
        Note note = new Note(title, content, date);
        note.id = id;
        note.folderId = folderId == -1 ? null : folderId;
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    // validation logic belongs in ViewModel — not in Activity
    public boolean isValidNote(String title) {
        return title != null && !title.trim().isEmpty();
    }
}