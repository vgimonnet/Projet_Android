package com.example.projet_android_lp.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ArtisteWithMusiques {
    @Embedded
    public Artiste artiste;
    @Relation(
            parentColumn = "artisteId",
            entityColumn = "artisteRefId"
    )
    public List<Musique> musiques;

}
