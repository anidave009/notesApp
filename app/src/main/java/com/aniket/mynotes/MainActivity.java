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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Note> fakeNotes = new ArrayList<>();
        fakeNotes.add(new Note("Grocery List", "Milk, eggs, bread, butter and coffee", "Jan 10, 2025"));
        fakeNotes.add(new Note("Meeting Notes", "Discuss Q1 targets with the team on Monday", "Jan 11, 2025"));
        fakeNotes.add(new Note("Book Ideas", "Write about personal finance for beginners", "Jan 12, 2025"));
        fakeNotes.add(new Note("Workout Plan", "Monday legs, Wednesday chest, Friday back", "Jan 13, 2025"));
        fakeNotes.add(new Note("Travel Plans", "Mumbai to Goa road trip in February", "Jan 14, 2025"));
        fakeNotes.add(new Note("App Ideas", "Notes app, expense tracker, habit tracker", "Jan 15, 2025"));
        fakeNotes.add(new Note("Quick Reminder", "Pay electricity bill before the 20th", "Jan 16, 2025"));

        RecyclerView recyclerView = findViewById(R.id.notesListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // CHANGE 1: Adapter now takes a second argument — the click listener
        // When a card is tapped, this lambda runs with that note as the argument
        recyclerView.setAdapter(new NotesAdapter(fakeNotes, note -> {
            // CHANGE 2: Create an Intent pointing to DetailActivity
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            // CHANGE 3: Attach the note's data as extras (key-value pairs)
            intent.putExtra("NOTE_TITLE", note.title);
            intent.putExtra("NOTE_CONTENT", note.content);
            intent.putExtra("NOTE_DATE", note.date);
            // CHANGE 4: Launch DetailActivity with that data
            startActivity(intent);
        }));
//        new NotesAdapter(fakeNotes, new NotesAdapter.OnNoteClickListener() {
//            @Override
//            public void onNoteClick(Note note) {
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                startActivity(intent);
//            }
//        });
        // CHANGE 5: FAB click — opens DetailActivity with NO data (blank new note)
        FloatingActionButton fab = findViewById(R.id.fabOptions);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            startActivity(intent);
        });
    }
}