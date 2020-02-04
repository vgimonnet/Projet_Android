package com.example.projet_android_lp.Utils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.ArtisteWithMusiques;
import com.example.projet_android_lp.Models.Musique;

import java.util.List;

@Dao
public interface ArtisteDAO {
    @Insert
    void insertArtiste(Artiste artiste);

    @Delete
    void delete(Artiste artiste);

    @Query("DELETE FROM artiste_table")
    void deleteAllArtiste();

    @Query("SELECT * FROM artiste_table ORDER BY artisteId ASC")
    LiveData<List<Artiste>> getAllArtistes();

    @Query("SELECT * FROM artiste_table ORDER BY artisteId ASC")
    List<Artiste> getAllArtistesWithoutLD();

    @Query("SELECT count(*) FROM artiste_table")
    int nbArtiste();

    @Query("SELECT count(*) FROM artiste_table")
    LiveData<Integer> nbArtisteLD();

    @Transaction
    @Query("SELECT * FROM artiste_table")
    LiveData<List<ArtisteWithMusiques>> getArtisteWithPlaylistsLD();

    @Transaction
    @Query("SELECT * FROM artiste_table")
    List<ArtisteWithMusiques> getArtisteWithPlaylists();

    @Query("SELECT * FROM artiste_table WHERE artisteId=:id LIMIT 1")
    Artiste getArtisteById(long id);

    @Query("SELECT * FROM artiste_table WHERE nom=:nom LIMIT 1")
    Artiste getArtisteByNom(String nom);
}
