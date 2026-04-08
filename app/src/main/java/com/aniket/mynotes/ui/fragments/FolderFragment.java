package com.aniket.mynotes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.R;
import com.aniket.mynotes.model.Folder;
import com.aniket.mynotes.ui.FolderAdapter;
import com.aniket.mynotes.viewmodel.FolderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FolderFragment extends Fragment {

    private FolderViewModel folderViewModel;

    public FolderFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        ImageView emptyImage = view.findViewById(R.id.emptyImage);
        TextView emptyText = view.findViewById(R.id.emptyText);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        FolderAdapter adapter = new FolderAdapter(new ArrayList<>(),
                folderrow -> {
                    // Navigate using Navigation Component
                    Bundle bundle = new Bundle();
                    bundle.putInt("folderId",folderrow.id);
                    bundle.putString("folderName", folderrow.folderName);

                    Navigation.findNavController(view)
                            .navigate(R.id.action_folderFragment_to_folderDetail, bundle);
                },
                (folder, anchorView) -> {
                    PopupMenu popup = new PopupMenu(requireContext(), anchorView);
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

        // Swipe to delete
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                ) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        Folder swipedFolder = adapter.getFolderAt(viewHolder.getBindingAdapterPosition());

                        new AlertDialog.Builder(requireContext())
                                .setTitle("Delete folder?")
                                .setMessage("Folder is deleted and notes will reflect on home screen.")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    folderViewModel.delete(swipedFolder);
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> {
                                    adapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
                                })
                                .setCancelable(false)
                                .show();
                    }
                };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView);

        folderViewModel.getAllFolders().observe(getViewLifecycleOwner(), folders -> {
            adapter.updateFolders(folders);

            if (folders.isEmpty()) {
                emptyImage.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyImage.setVisibility(View.GONE);
                emptyText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAddNewNote);
        fab.setOnClickListener(v -> showCreateFolderDialog());

        return view;
    }

    private void showRenameDialog(Folder folder) {
        EditText input = new EditText(requireContext());
        input.setText(folder.folderName);
        input.setSelection(folder.folderName.length());

        new AlertDialog.Builder(requireContext())
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
        EditText input = new EditText(requireContext());
        input.setHint("Folder name");

        new AlertDialog.Builder(requireContext())
                .setTitle("New Folder")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (folderViewModel.isValidFolder(name)) {
                        folderViewModel.insert(name);
                    } else {
                        Toast.makeText(requireContext(),
                                "Folder name cannot be empty",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}