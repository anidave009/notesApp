package com.aniket.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.aniket.mynotes.viewmodel.NoteViewModel;
import com.aniket.mynotes.R;

public class DetailActivity extends AppCompatActivity {

    NoteViewModel noteViewModel;
    int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        EditText editTitle = findViewById(R.id.etTitle);
        EditText editContent = findViewById(R.id.etContent);

        // get the same ViewModel — but scoped to this Activity
        // each Activity gets its own ViewModel instance
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        String title = getIntent().getStringExtra("NOTE_TITLE");
        String content = getIntent().getStringExtra("NOTE_CONTENT");
        noteId = getIntent().getIntExtra("NOTE_ID", -1);

        if (title != null) editTitle.setText(title);
        if (content != null) editContent.setText(content);

        Button saveBtn = findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(v -> {

            String newTitle = editTitle.getText().toString();
            String newContent = editContent.getText().toString();

            if (!noteViewModel.isValidNote(newTitle)) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noteId == -1) {
                // new note — insert
                noteViewModel.insert(newTitle, newContent);
            } else {
                // existing note — update
                noteViewModel.update(noteId, newTitle, newContent);
            }

            finish();
        });

        ImageButton backBtn=findViewById(R.id.btnBack);
        backBtn.setOnClickListener(v->{
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
