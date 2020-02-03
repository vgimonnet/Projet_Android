package com.example.projet_android_lp.Utils;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.ArtisteWithMusiques;
import com.example.projet_android_lp.Models.Musique;

import java.util.List;

@Dao
public interface MusiqueDAO {
    @Insert
    void insert(Musique musique);

    @Delete
    void delete(Musique musique);

    @Update
    void update(Musique musique);

    @Query("DELETE FROM musique_table")
    void deleteAll();

    @Query("SELECT * from musique_table ORDER BY id ASC")
    LiveData<List<Musique>> getAllMusiques();

    @Query("SELECT count(*) from musique_table")
    int nbElements();

    @Query("SELECT count(*) from musique_table")
    LiveData<Integer> nbElementsLD();

    @Query("SELECT * FROM musique_table WHERE id=:id LIMIT 1")
    Musique getMusiqueById(Long id);

    @Transaction
    @Query("SELECT * FROM artiste_table")
    List<ArtisteWithMusiques> getArtisteWithPlaylists();

    @Transaction
    @Query("SELECT * FROM artiste_table")
    LiveData<List<ArtisteWithMusiques>> getArtisteWithPlaylistsLD();


}
