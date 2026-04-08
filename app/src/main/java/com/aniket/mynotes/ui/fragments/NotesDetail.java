package com.aniket.mynotes.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.aniket.mynotes.R;
import com.aniket.mynotes.viewmodel.NoteViewModel;

public class NotesDetail extends Fragment {

    private NoteViewModel noteViewModel;

    private int folderId = -1;
    private int noteId = -1;

    public NotesDetail() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes_detail, container, false);

        EditText editTitle = view.findViewById(R.id.etTitle);
        EditText editContent = view.findViewById(R.id.etContent);
        Button saveBtn = view.findViewById(R.id.btnSave);
        ImageButton backBtn = view.findViewById(R.id.btnBack);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        //  Get arguments instead of Intent
        Bundle args = getArguments();
        if (args != null) {
            folderId = args.getInt("folderId", -1);
            noteId = args.getInt("noteId", -1);

            String title = args.getString("title");
            String content = args.getString("content");

            if (title != null) editTitle.setText(title);
            if (content != null) editContent.setText(content);
        }

        saveBtn.setOnClickListener(v -> {

            String newTitle = editTitle.getText().toString().trim();
            String newContent = editContent.getText().toString().trim();

            if (!noteViewModel.isValidNote(newTitle)) {
                Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noteId == -1) {
                if (folderId != -1) {
                    noteViewModel.insert(newTitle, newContent, folderId);
                } else {
                    noteViewModel.insert(newTitle, newContent);
                }
            } else {
                noteViewModel.update(noteId, newTitle, newContent, folderId);
            }

            //  instead of finish()
            Navigation.findNavController(view).navigateUp();
        });

        backBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        return view;
    }
}