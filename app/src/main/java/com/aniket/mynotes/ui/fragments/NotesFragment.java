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
import com.aniket.mynotes.ui.NotesAdapter;
import com.aniket.mynotes.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesFragment extends Fragment {

    NoteViewModel noteViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // inflate fragment layout — same as setContentView in Activity
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // view.findViewById — not just findViewById like in Activity
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ImageView emptyImage = view.findViewById(R.id.emptyImage);
        TextView emptyText = view.findViewById(R.id.emptyText);
        FloatingActionButton fab = view.findViewById(R.id.fabAddNewNote);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        NotesAdapter adapter = new NotesAdapter(new java.util.ArrayList<>(), note -> {
            // navigate to NoteDetailFragment using nav graph action
            // pass note data as arguments via Bundle
            Bundle bundle = new Bundle();
            bundle.putInt("noteId", note.id);
            bundle.putInt("folderId", note.folderId);
            bundle.putString("title", note.title);
            bundle.putString("content", note.content);

            Navigation.findNavController(view)
                    .navigate(R.id.action_notesFragment_to_notesDetail, bundle);
        });

        recyclerView.setAdapter(adapter);

        // ViewModel scoped to Activity — shared across all fragments
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
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

        // FAB navigates to NoteDetailFragment with no arguments — new note
        fab.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_notesFragment_to_notesDetail)
        );

        // swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv,
                                  @NonNull RecyclerView.ViewHolder vh,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getBindingAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        // three dot options popup
        ImageButton optionsBtn = view.findViewById(R.id.threedotoptions);
        optionsBtn.setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popup =
                    new androidx.appcompat.widget.PopupMenu(requireContext(), v);
            popup.inflate(R.menu.menu_options);
            popup.setOnMenuItemClickListener(item -> false);
            popup.show();
        });
    }
}