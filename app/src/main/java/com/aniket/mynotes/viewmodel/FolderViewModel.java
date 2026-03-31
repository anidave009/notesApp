package com.aniket.mynotes.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aniket.mynotes.model.Folder;
import com.aniket.mynotes.repository.FolderRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FolderViewModel extends AndroidViewModel {

    private FolderRepository repository;
    private LiveData<List<Folder>> allFolders;

    public FolderViewModel(@NonNull Application application) {
        super(application);
        repository = new FolderRepository(application);
        allFolders = repository.getAllFolders();
    }

    public LiveData<List<Folder>> getAllFolders() {
        return allFolders;
    }

    public void insert(String name) {
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(new Date());
        repository.insert(new Folder(name, date));
    }

    public void delete(Folder folder) {
        repository.delete(folder);
    }

    public boolean isValidFolder(String name) {
        return name != null && !name.trim().isEmpty();
    }
}