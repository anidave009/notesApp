package com.aniket.mynotes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aniket.mynotes.R;
import com.aniket.mynotes.model.Folder;
import com.aniket.mynotes.model.Folder;

import java.util.Collections;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    List<Folder> folderList;
    OnFolderClickListener listener;
    OnFolderLongClickListener longClickListener;

    public interface OnFolderClickListener {
        void onFolderClick(Folder folder);
    }

    public interface OnFolderLongClickListener {
        void onFolderLongClick(Folder folder, View anchorView);
    }
    public FolderAdapter(List<Folder> folderList, OnFolderClickListener listener,
                         OnFolderLongClickListener longClickListener) {
        this.folderList = folderList;
        this.listener = listener;
        this.longClickListener=longClickListener;
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
        Folder folderrow=folderList.get(position);
        holder.folderName.setText(folderrow.folderName);
        holder.fileCount.setText("0");
        holder.itemView.setOnClickListener(v -> listener.onFolderClick(folderrow));
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onFolderLongClick(folderrow, v);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public Folder getFolderAt(int position) {
        return folderList.get(position);
    }
    public void updateFolders(List<Folder> newFolders) {
        this.folderList = newFolders;
        notifyDataSetChanged();
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
