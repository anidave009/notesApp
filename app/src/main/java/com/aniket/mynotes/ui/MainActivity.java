package com.aniket.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

        ImageView emptyImage= findViewById(R.id.emptyImage);
        TextView emptyText=findViewById(R.id.emptyText);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
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
            if (notes == null || notes.isEmpty()) {
                emptyImage.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.VISIBLE);
            } else {
                emptyImage.setVisibility(View.GONE);
                emptyText.setVisibility(View.GONE);
            }
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
}