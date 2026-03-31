package com.aniket.mynotes.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
    (tableName = "notes_table",
    foreignKeys = @ForeignKey(
    entity = Folder.class,
    parentColumns = "id",
    childColumns = "folderId",
    onDelete = ForeignKey.SET_NULL
    )
)
public class Note{
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String content;
    public String date;
    public Integer folderId;

    public Note(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }
}
