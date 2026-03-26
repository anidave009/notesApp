package com.aniket.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.viewmodel.NoteViewModel;
import com.aniket.mynotes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.notesListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NotesAdapter adapter = new NotesAdapter(new java.util.ArrayList<>(), note -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("NOTE_TITLE", note.title);
            intent.putExtra("NOTE_CONTENT", note.content);
            intent.putExtra("NOTE_ID", note.id);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> {
            adapter.updateNotes(notes);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddNewNote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DetailActivity.class));
        });
    }
}
