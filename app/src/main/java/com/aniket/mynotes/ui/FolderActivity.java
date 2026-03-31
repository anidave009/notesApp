package com.aniket.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.R;
import com.aniket.mynotes.model.FolderModel;
import com.aniket.mynotes.viewmodel.FolderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FolderActivity extends AppCompatActivity {
    FolderViewModel folderViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FolderAdapter adapter = new FolderAdapter(new ArrayList<>(), folder -> {
            // navigate to folder detail — coming next
            Intent intent = new Intent(FolderActivity.this, FolderDetailActivity.class);
            intent.putExtra("FOLDER_ID", folder.id);
            intent.putExtra("FOLDER_NAME", folder.folderName);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);
        folderViewModel.getAllFolders().observe(this, folders -> {
            adapter.updateFolders(folders);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddNewNote);
        fab.setOnClickListener(v -> showCreateFolderDialog());

        // bottom tab — folder tab is already here, home goes back to MainActivity
        ImageButton homeBtn = findViewById(R.id.btnHome);
        homeBtn.setOnClickListener(v -> {
            startActivity(new Intent(FolderActivity.this, MainActivity.class));
        });
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
