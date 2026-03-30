package com.aniket.mynotes.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.R;
import com.aniket.mynotes.model.FolderModel;

import java.util.ArrayList;
import java.util.List;

public class FolderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<FolderModel> folderModelList = new ArrayList<>();

        folderModelList.add(new FolderModel("5", "Work"));
        folderModelList.add(new FolderModel("12", "Personal"));
        folderModelList.add(new FolderModel("3", "Ideas"));
        folderModelList.add(new FolderModel("8", "Projects"));
        folderModelList.add(new FolderModel("2", "Travel"));
        recyclerView.setAdapter(new FolderAdapter(folderModelList));
    }
}
