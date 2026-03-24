package com.aniket.mynotes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
    private static NoteDatabase instance;

    public static NoteDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(
              context.getApplicationContext(),
              NoteDatabase.class,
              "notes_database"
            )
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
