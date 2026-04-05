package com.aniket.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.R;
import com.aniket.mynotes.model.Folder;
import com.aniket.mynotes.viewmodel.FolderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {
    FolderViewModel folderViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FolderAdapter adapter = new FolderAdapter(new ArrayList<>(), folderrow -> {
            // navigate to folder detail — coming next
            Intent intent = new Intent(FolderActivity.this, FolderDetailActivity.class);
            intent.putExtra("FOLDER_ID", folderrow.id);
            intent.putExtra("FOLDER_NAME", folderrow.folderName);
            startActivity(intent);
        },(folder, anchorView) -> {
            PopupMenu popup = new PopupMenu(FolderActivity.this, anchorView);
            popup.inflate(R.menu.menu_folder_options);

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_rename) {
                    showRenameDialog(folder);
                    return true;
                }
                return false;
            });

            popup.show();
        });
        recyclerView.setAdapter(adapter);
        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);

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
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        Folder swipedFolder = adapter.getFolderAt(viewHolder.getBindingAdapterPosition());

                        new AlertDialog.Builder(FolderActivity.this)
                                .setTitle("Delete folder?")
                                .setMessage("Folder is deleted and notes will reflect on home screen .")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    folderViewModel.delete(swipedFolder);
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> {
                                    // user cancelled — put the card back
                                    adapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
                                })
                                .setCancelable(false)
                                .show();
                    }
                };
        // Step 2: Create ItemTouchHelper object
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);

        // Step 3: Attach it to RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView);

        folderViewModel.getAllFolders().observe(this, folders -> {
            adapter.updateFolders(folders);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddNewNote);
        fab.setOnClickListener(v -> showCreateFolderDialog());

        // bottom tab — folder tab is already here, home goes back to MainActivity
        ImageButton homeBtn = findViewById(R.id.btnHome);
        homeBtn.setOnClickListener(v -> {
            finish();
        });
    }
    private void showRenameDialog(Folder folder) {
        EditText input = new EditText(this);
        input.setText(folder.folderName);  // pre-fill current name
        input.setSelection(folder.folderName.length()); // cursor at end
        int px = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(px, px, px, px);

        new AlertDialog.Builder(this)
                .setTitle("Rename folder")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        folder.folderName = newName;
                        folderViewModel.update(folder);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showCreateFolderDialog() {
        EditText input = new EditText(this);
        input.setHint("Folder name");
        input.setTextColor(getResources().getColor(android.R.color.black));

        // padding so EditText doesn't touch dialog edges
        int px = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(px, px, px, px);

        new AlertDialog.Builder(this)
                .setTitle("New Folder")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (folderViewModel.isValidFolder(name)) {
                        folderViewModel.insert(name);
                    } else {
                        Toast.makeText(this,
                                "Folder name cannot be empty",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
