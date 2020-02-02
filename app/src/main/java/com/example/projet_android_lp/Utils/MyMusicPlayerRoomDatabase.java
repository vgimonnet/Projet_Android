package com.example.projet_android_lp.Utils;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.Initialisation;
import com.example.projet_android_lp.Models.Musique;

@Database(entities = {Musique.class, Artiste.class, Initialisation.class}, version = 1, exportSchema = false)
public abstract class MyMusicPlayerRoomDatabase extends RoomDatabase {

    public abstract MusiqueDAO musiqueDAO();
    public abstract ArtisteDAO artisteDAO();
    public abstract InitialisationDAO initialisationDAO();
    private static MyMusicPlayerRoomDatabase INSTANCE;

    static MyMusicPlayerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyMusicPlayerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyMusicPlayerRoomDatabase.class, "mymusicplayer_database").addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MusiqueDAO musiqueDAO;
        private final ArtisteDAO artisteDAO;
        private final InitialisationDAO initialisationDAO;


        PopulateDbAsync(MyMusicPlayerRoomDatabase db) {
            musiqueDAO = db.musiqueDAO();
            artisteDAO = db.artisteDAO();
            initialisationDAO = db.initialisationDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            if (initialisationDAO.nbInitialisation() == 0){
                Initialisation initialisation = new Initialisation(true);
                initialisationDAO.insert(initialisation);

                Artiste artiste = new Artiste("Slipknot");
                artisteDAO.insertArtiste(artiste);
                artiste = new Artiste("Pennywise");
                artisteDAO.insertArtiste(artiste);

                Musique musique = new Musique(0, "The Devil In I", "The Grey Chapter", 2014, "Metal");
                musiqueDAO.insert(musique);
                musique = new Musique(0, "Unsainted", "We Are Not Your Kind", 2019, "Metal");
                musiqueDAO.insert(musique);
                musique = new Musique(1, "Fuck Authority!", "Land Of The Free?", 2001, "Punk");
                musiqueDAO.insert(musique);

            }
            return null;
        }
    }

}
