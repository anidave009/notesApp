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

// AndroidViewModel is used instead of ViewModel because we need
// Application context to create the Repository
// never use Activity context here — Activity can be destroyed
// Application context lives as long as the app lives
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

    // all business logic lives here — not in Activity

    public void insert(String title, String content) {
        // date formatting is business logic — belongs in ViewModel not Activity
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(new Date());
        repository.insert(new Note(title, content, date));
    }

    public void update(int id, String title, String content) {
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(new Date());
        Note note = new Note(title, content, date);
        // must set id so Room knows which row to update
        note.id = id;
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