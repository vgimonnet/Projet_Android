package com.example.projet_android_lp.Utils;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projet_android_lp.Models.Musique;

import java.util.List;

@Dao
public interface MusiqueDAO {
    @Insert
    void insert(Musique musique);

    @Query("DELETE FROM musique_table")
    void deleteAll();

    @Query("SELECT * from musique_table ORDER BY id ASC")
    LiveData<List<Musique>> getAllMusiques();

    @Query("SELECT count(*) from musique_table")
    int nbElements();

    @Query("SELECT count(*) from musique_table")
    LiveData<Integer> nbElementsLD();

}
