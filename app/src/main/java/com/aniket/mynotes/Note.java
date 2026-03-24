package com.aniket.mynotes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class Note{
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String content;
    public String date;

    public Note(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }
}
