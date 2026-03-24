// MainActivity.java
package com.aniket.mynotes;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NoteDatabase db;
    NotesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = NoteDatabase.getInstance(this);

        List<Note> notes = db.noteDao().getAllNotes();//NoteDao object gets created and implemented by
        //room in runtime and that is why i can use getAllNotes method od NoteDao class.    
        RecyclerView recyclerView = findViewById(R.id.notesListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotesAdapter(notes, note -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("NOTE_TITLE", note.title);
            intent.putExtra("NOTE_CONTENT", note.content);
            // pass the id so DetailActivity knows which note to UPDATE
            // without id, DetailActivity would always INSERT a new note
            intent.putExtra("NOTE_ID", note.id);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        // CHANGE 5: FAB click — opens DetailActivity with NO data (blank new note)
        FloatingActionButton fab = findViewById(R.id.fabOptions);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // reload from database and tell adapter the data changed
        List<Note> updatedNotes = db.noteDao().getAllNotes();
        adapter.updateNotes(updatedNotes);
    }
}

// CHANGE 1: Adapter now takes a second argument — the click listener
// When a card is tapped, this lambda runs with that note as the argument
//        recyclerView.setAdapter(new NotesAdapter(fakeNotes, note -> {
// CHANGE 2: Create an Intent pointing to DetailActivity
//Intent intent = new Intent(MainActivity.this, DetailActivity.class);
// CHANGE 3: Attach the note's data as extras (key-value pairs)
//            intent.putExtra("NOTE_TITLE", note.title);
//            intent.putExtra("NOTE_CONTENT", note.content);
//            intent.putExtra("NOTE_DATE", note.date);
// CHANGE 4: Launch DetailActivity with that data
//startActivity(intent);
//        }));
//        new NotesAdapter(fakeNotes, new NotesAdapter.OnNoteClickListener() {
//            @Override
//            public void onNoteClick(Note note) {
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                startActivity(intent);
//            }
//        });