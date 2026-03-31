package com.aniket.mynotes.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.aniket.mynotes.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
    @Query("SELECT * FROM notes_table ORDER BY id desc")
    LiveData<List<Note>> getAllNotes(
    );
    // Notes inside a specific folder — used by FolderDetailActivity
    @Query("SELECT * FROM notes_table WHERE folderId = :folderId ORDER BY id DESC")
    LiveData<List<Note>> getNotesByFolder(int folderId);
}
