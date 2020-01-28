package com.example.projet_android_lp.Utils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.ArtisteWithMusiques;

import java.util.List;

@Dao
public interface ArtisteDAO {
    @Insert
    void insertArtiste(Artiste artiste);

    @Query("DELETE FROM artiste_table")
    void deleteAllArtiste();

    @Query("SELECT * from artiste_table ORDER BY id ASC")
    LiveData<List<Artiste>> getAllArtistes();

    @Query("SELECT count(*) from artiste_table")
    int nbArtiste();

    @Query("SELECT count(*) from musique_table")
    LiveData<Integer> nbArtisteLD();

    @Transaction
    @Query("SELECT * FROM artiste_table")
    LiveData<List<ArtisteWithMusiques>> getArtisteWithPlaylistsLD();

    @Transaction
    @Query("SELECT * FROM artiste_table")
    List<ArtisteWithMusiques> getArtisteWithPlaylists();
}
