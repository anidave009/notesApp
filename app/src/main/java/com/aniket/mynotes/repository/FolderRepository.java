package com.aniket.mynotes.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.aniket.mynotes.db.FolderDao;
import com.aniket.mynotes.db.NoteDatabase;
import com.aniket.mynotes.model.Folder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FolderRepository {

    private FolderDao folderDao;
    private LiveData<List<Folder>> allFolders;
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    public FolderRepository(Application application) {
        NoteDatabase db = NoteDatabase.getInstance(application);
        folderDao = db.folderDao();
        allFolders = folderDao.getAllFolders();
    }

    public LiveData<List<Folder>> getAllFolders() {
        return allFolders;
    }

    public void insert(Folder folder) {
        executor.execute(() -> folderDao.insert(folder));
    }

    public void delete(Folder folder) {
        executor.execute(() -> folderDao.delete(folder));
    }
}