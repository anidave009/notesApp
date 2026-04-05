package com.aniket.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.R;
import com.aniket.mynotes.model.Note;
import com.aniket.mynotes.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


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
        // Step 1: Create the callback separately
        ItemTouchHelper.SimpleCallback swipeToDeleteCallback =
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP |ItemTouchHelper.DOWN, // no drag
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT // allow swipe
                ) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {

                        int fromPosition = viewHolder.getBindingAdapterPosition();
                        int toPosition = target.getBindingAdapterPosition();

                        adapter.swapItems(fromPosition, toPosition);
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        Note deletedNote = adapter.getNoteAt(viewHolder.getBindingAdapterPosition());
                        noteViewModel.delete(deletedNote);

                        Snackbar.make(recyclerView, "Note deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> noteViewModel.insert(
                                        deletedNote.title,
                                        deletedNote.content,
                                        deletedNote.folderId  // preserve folder if it had one
                                ))
                                .show();
                    }
                };
        // Step 2: Create ItemTouchHelper object
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);

        // Step 3: Attach it to RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView);


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
