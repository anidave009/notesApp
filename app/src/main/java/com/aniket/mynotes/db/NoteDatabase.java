package com.aniket.mynotes.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.aniket.mynotes.model.Note;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
    private static NoteDatabase instance;

    public static NoteDatabase getInstance(Context context){
        if(instance==null){
        Log.d("noteDB","hitting the db");
            instance= Room.databaseBuilder(
              context.getApplicationContext(),
              NoteDatabase.class,
              "notes_database"
            )
                    .build();
        }
        return instance;
    }
}
