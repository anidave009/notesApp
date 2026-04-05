package com.aniket.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.R;
import com.aniket.mynotes.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FolderDetailActivity extends AppCompatActivity {

    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folderdetail);
        int folderId = getIntent().getIntExtra("FOLDER_ID", -1);
        String folderName = getIntent().getStringExtra("FOLDER_NAME");

        // show folder name as the screen title
        TextView title = findViewById(R.id.folderTitle);
        title.setText(folderName);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter starts with empty list — LiveData will populate it
        NotesAdapter adapter = new NotesAdapter(new java.util.ArrayList<>(), note -> {
            Intent intent = new Intent(FolderDetailActivity.this, DetailActivity.class);
            intent.putExtra("NOTE_TITLE", note.title);
            intent.putExtra("NOTE_CONTENT", note.content);
            intent.putExtra("NOTE_ID", note.id);
            intent.putExtra("FOLDER_ID",folderId);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        noteViewModel.getNotesByFolder(folderId).observe(this, notes -> {
            adapter.updateNotes(notes);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddNewNote);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(FolderDetailActivity.this, DetailActivity.class);
            intent.putExtra("FOLDER_ID", folderId);
            startActivity(intent);
        });

        ImageButton btnBack= findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            finish();
        });
    }
}
