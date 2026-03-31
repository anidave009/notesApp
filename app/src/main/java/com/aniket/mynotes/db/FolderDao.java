package com.aniket.mynotes.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.aniket.mynotes.model.Folder;
import java.util.List;

@Dao
public interface FolderDao {

    @Insert
    void insert(Folder folder);

    @Delete
    void delete(Folder folder);

    @Query("SELECT * FROM folders_table ORDER BY id DESC")
    LiveData<List<Folder>> getAllFolders();
}