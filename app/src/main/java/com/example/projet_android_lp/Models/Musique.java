package com.example.projet_android_lp.Models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "musique_table")

public class Musique {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "artiste")
    private String artiste;

    @NonNull
    @ColumnInfo(name = "titre")
    private String titre;

    @ColumnInfo(name = "album")
    private String album;

    @ColumnInfo(name = "annee")
    private int annee;

    @ColumnInfo(name = "genre")
    private String genre;


    public Musique(String artiste, String titre, String album, int annee, String genre) {
        this.artiste = artiste;
        this.titre = titre;
        this.album = album;
        this.annee = annee;
        this.genre = genre;
    }

    @Ignore
    public Musique() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
