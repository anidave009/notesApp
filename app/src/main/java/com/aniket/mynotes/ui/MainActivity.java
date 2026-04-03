package com.aniket.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.model.Note;
import com.aniket.mynotes.viewmodel.NoteViewModel;
import com.aniket.mynotes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter starts with empty list — LiveData will populate it
        //concept of anonymous class , lambda is used over here .
        NotesAdapter adapter = new NotesAdapter(new java.util.ArrayList<>(), note -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("NOTE_TITLE", note.title);
            intent.putExtra("NOTE_CONTENT", note.content);
            intent.putExtra("NOTE_ID", note.id);
            startActivity(intent);
        });

        //same way of writing the code without using lambda fn .
//        NotesAdapter adapter = new NotesAdapter(
//                new ArrayList<>(),
//                new NotesAdapter.OnNoteClickListener() {
//                    @Override
//                    public void onNoteClick(Note note) {
//                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                        intent.putExtra("NOTE_TITLE", note.title);
//                        intent.putExtra("NOTE_CONTENT", note.content);
//                        intent.putExtra("NOTE_ID", note.id);
//                        startActivity(intent);
//                    }
//                }
//        );


        recyclerView.setAdapter(adapter);

        // ViewModelProvider creates the ViewModel OR returns the existing one
        // if Activity is recreated (rotation), the same ViewModel instance is returned
        // this is how data survives rotation
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // observe means: whenever the notes list changes in the database,
        // run this code automatically — no manual refresh needed
        // onResume() with manual reload is gone — LiveData replaces it
        noteViewModel.getAllNotes().observe(this, notes -> {
            // this runs automatically whenever Room data changes
            adapter.updateNotes(notes);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddNewNote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DetailActivity.class));
        });

        ImageButton folderNavigation=findViewById(R.id.folderNavigation);
        folderNavigation.setOnClickListener(v->{
            Log.d("MainActivity","folderClick log");
            startActivity(new Intent(MainActivity.this,FolderActivity.class));
        });


    }

    // onResume() with manual db reload is GONE
    // LiveData handles all of this automatically now
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



//class MyClickListener implements NotesAdapter.OnNoteClickListener {
//    @Override
//    public void onNoteClick(Note note) {
//        // logic
//    }
//}
//NotesAdapter.OnNoteClickListener listener =
//        new NotesAdapter.OnNoteClickListener() {
//            @Override
//            public void onNoteClick(Note note) {
//                // logic
//            }
//        };