package com.example.projet_android_lp.Utils;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.Initialisation;

import java.util.List;

@Dao
public interface InitialisationDAO {
    @Insert
    void insert(Initialisation initialisation);

    @Query("SELECT * FROM initialisation WHERE id=0 LIMIT 1")
    Initialisation getFirstInitialisation();

    @Query("DELETE FROM initialisation")
    void deleteAll();

    @Query("SELECT count(*) FROM initialisation")
    int nbInitialisation();
}
