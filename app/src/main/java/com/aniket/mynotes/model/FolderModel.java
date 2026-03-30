package com.aniket.mynotes.model;

public class FolderModel {
    public String folderName;
    public String fileCount;

    public FolderModel(String fileCount, String folderName) {
        this.fileCount = fileCount;
        this.folderName = folderName;
    }

    public void setfileCount(String fileCount) {
        this.fileCount = fileCount;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
