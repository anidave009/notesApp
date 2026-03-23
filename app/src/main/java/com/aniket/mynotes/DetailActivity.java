// DetailActivity.java
package com.aniket.mynotes;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        EditText editTitle = findViewById(R.id.etTitle);
        EditText editContent = findViewById(R.id.etContent);

        // CHANGE 1: Read whatever was attached to the Intent that opened this screen
        String title = getIntent().getStringExtra("NOTE_TITLE");
        String content = getIntent().getStringExtra("NOTE_CONTENT");

        // CHANGE 2: If title is null it means FAB was tapped (new note) — leave blank
        // If title is not null it means a card was tapped — fill the fields
        if (title != null) editTitle.setText(title);
        if (content != null) editContent.setText(content);

        // Save button is a dummy for now — just goes back
        // We will wire real saving here once Room is set up
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}