package com.aniket.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.model.Note;
import com.aniket.mynotes.viewmodel.NoteViewModel;
import com.aniket.mynotes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView emptyImage= findViewById(R.id.emptyImage);
        TextView emptyText=findViewById(R.id.emptyText);
        ImageButton options=findViewById(R.id.threedotoptions);
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

        options.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v); // v is the button itself — anchor
            popup.inflate(R.menu.menu_options);       // your menu xml
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_sort) {
                    // handle sort
                    return true;
                } else if (id == R.id.action_select_all) {
                    // handle select all
                    return true;
                } else if (id == R.id.action_settings) {
                    // handle settings
                    return true;
                }
                return false;
            });
            popup.show();
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