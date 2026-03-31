package com.aniket.mynotes.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "folders_table")
public class Folder {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String folderName;
    public String date;

    public Folder(String folderName,String date){
        this.folderName=folderName;
        this.date=date;
    }
}
