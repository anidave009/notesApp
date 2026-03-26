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

            // validation logic is in ViewModel — Activity just calls it
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

            // go back — LiveData in MainActivity will auto-update the list
            finish();
        });

        ImageButton backBtn=findViewById(R.id.btnBack);
        backBtn.setOnClickListener(v->{
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}


//package com.aniket.mynotes;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//public class DetailActivity extends AppCompatActivity {
//
//    NoteDatabase db;
//    int noteId = -1; // -1 means no id — this is a new note
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//
//        db = NoteDatabase.getInstance(this);
//
//        EditText editTitle = findViewById(R.id.etTitle);
//        EditText editContent = findViewById(R.id.etContent);
//
//        // check if an existing note was passed in
//        String title = getIntent().getStringExtra("NOTE_TITLE");
//        String content = getIntent().getStringExtra("NOTE_CONTENT");
//        noteId = getIntent().getIntExtra("NOTE_ID", -1);
//        // getIntExtra needs a default value — we use -1 to mean "no id / new note"
//
//        if (title != null) editTitle.setText(title);
//        if (content != null) editContent.setText(content);
//
//        Button saveBtn = findViewById(R.id.btnSave);
//        saveBtn.setOnClickListener(v -> {
//
//            String newTitle = editTitle.getText().toString().trim();
//            String newContent = editContent.getText().toString().trim();
//
//            // don't save empty notes
//            if (newTitle.isEmpty()) {
//                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // generate today's date as a readable string
//            String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
//                    .format(new Date());
//
//            if (noteId == -1) {
//                // noteId is -1 means FAB was tapped — INSERT a new note
//                Note newNote = new Note(newTitle, newContent, date);
//                db.noteDao().insert(newNote);
//            } else {
//                // noteId exists — UPDATE the existing note
//                Note updatedNote = new Note(newTitle, newContent, date);
//                updatedNote.id = noteId; // must set id so Room knows which row to update
//                db.noteDao().update(updatedNote);
//            }
//            finish();
//        });
//
//        ImageButton backBtn=findViewById(R.id.btnBack);
//        backBtn.setOnClickListener(v->{
//                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
//                startActivity(intent);
//        });
//    }
//}