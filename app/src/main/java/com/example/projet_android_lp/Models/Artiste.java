package com.example.projet_android_lp.Models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artiste_table")

public class Artiste {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long artisteId;

    @NonNull
    @ColumnInfo(name = "nom")
    private String nom;

    public Artiste(@NonNull String nom) {
        this.nom = nom;
    }

    public long getArtisteId() {
        return artisteId;
    }

    public void setArtisteId(long artisteId) {
        artisteId = artisteId;
    }

    @NonNull
    public String getNom() {
        return nom;
    }

    public void setNom(@NonNull String nom) {
        this.nom = nom;
    }
}
