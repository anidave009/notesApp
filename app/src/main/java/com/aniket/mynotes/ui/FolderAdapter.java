package com.aniket.mynotes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.R;
import com.aniket.mynotes.model.FolderModel;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    List<FolderModel> folderModelList;
    public FolderAdapter(List<FolderModel> folderModelList){
        this.folderModelList=folderModelList;
    }
    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder,parent,false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.FolderViewHolder holder, int position) {
        FolderModel folderrow=folderModelList.get(position);
        holder.folderName.setText(folderrow.folderName);
        holder.fileCount.setText(folderrow.fileCount);
    }

    @Override
    public int getItemCount() {
        return folderModelList.size();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder{
        TextView folderName,fileCount;
        public FolderViewHolder(@NonNull View itemview){
            super(itemview);
            folderName=itemview.findViewById(R.id.folderName);
            fileCount=itemview.findViewById(R.id.fileCount);
        }
    }
}
