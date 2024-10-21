package com.example.projetws;
import android.net.Uri; // Ajoutez cette ligne en haut de votre fichier


public class DataPart {
    private String fileName;
    private Uri uri;

    public DataPart(String fileName, Uri uri) {
        this.fileName = fileName;
        this.uri = uri;
    }

    public String getFileName() {
        return fileName;
    }

    public Uri getUri() {
        return uri;
    }
}

