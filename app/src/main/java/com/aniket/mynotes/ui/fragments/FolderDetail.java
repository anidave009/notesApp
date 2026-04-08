package com.aniket.mynotes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.R;
import com.aniket.mynotes.model.Note;
import com.aniket.mynotes.ui.NotesAdapter;
import com.aniket.mynotes.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FolderDetail extends Fragment {

    private NoteViewModel noteViewModel;
    private int folderId = -1;
    private String folderName = "";

    public FolderDetail() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_folder_detail, container, false);

        TextView title = view.findViewById(R.id.folderTitle);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton fab = view.findViewById(R.id.fabAddNewNote);
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        ImageView emptyImage = view.findViewById(R.id.emptyImage);
        TextView emptyText = view.findViewById(R.id.emptyText);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Bundle args = getArguments();
        if (args != null) {
            folderId = args.getInt("folderId", -1);
            folderName = args.getString("folderName", "");
        }

        title.setText(folderName);

        NotesAdapter adapter = new NotesAdapter(new ArrayList<>(), note -> {

            Bundle bundle = new Bundle();
            bundle.putInt("noteId", note.id);
            bundle.putInt("folderId", folderId);
            bundle.putString("title", note.title);
            bundle.putString("content", note.content);

            Navigation.findNavController(view)
                    .navigate(R.id.action_folderDetailFragment_to_notesDetailFragment, bundle);
        });

        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // Swipe + Drag
        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                ) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {

                        int from = viewHolder.getBindingAdapterPosition();
                        int to = target.getBindingAdapterPosition();

                        adapter.swapItems(from, to);
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
                                        deletedNote.folderId
                                ))
                                .show();
                    }
                };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);

        noteViewModel.getNotesByFolder(folderId)
                .observe(getViewLifecycleOwner(), notes -> {
                    adapter.updateNotes(notes);

                    // empty state logic
                    if (notes.isEmpty()) {
                        emptyImage.setVisibility(View.VISIBLE);
                        emptyText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyImage.setVisibility(View.GONE);
                        emptyText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });

        fab.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("folderId", folderId);

            Navigation.findNavController(view)
                    .navigate(R.id.action_folderDetailFragment_to_notesDetailFragment, bundle);
        });

        btnBack.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        return view;
    }
}