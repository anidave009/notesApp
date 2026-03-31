package com.aniket.mynotes.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aniket.mynotes.model.Folder;
import com.aniket.mynotes.model.Note;

@Database(entities = {Note.class, Folder.class}, version = 2)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
    public abstract FolderDao folderDao();
    private static NoteDatabase instance;

    // Migration from version 1 to 2:
    // — creates the folders table
    // — adds nullable folderId column to notes_table
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS folders_table (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "folderName TEXT, " +
                            "date TEXT)"
            );
            database.execSQL(
                    "ALTER TABLE notes_table ADD COLUMN folderId INTEGER DEFAULT NULL " +
                            "REFERENCES folders_table(id) ON DELETE SET NULL"
            );
        }
    };

    public static NoteDatabase getInstance(Context context){
        if(instance==null){
        Log.d("noteDB","hitting the db");
            instance= Room.databaseBuilder(
              context.getApplicationContext(),
              NoteDatabase.class,
              "notes_database"
            )
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }
}

//we are building a database object to wrap up dao calls via this,
//a single db object is created for whole app , only connection to it
//is established and checked.