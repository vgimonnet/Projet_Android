package com.example.projet_android_lp.Utils;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.projet_android_lp.Models.Musique;

@Database(entities = {Musique.class}, version = 1, exportSchema = false)
public abstract class MyMusicPlayerRoomDatabase extends RoomDatabase {

    public abstract MusiqueDAO musiqueDAO();
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


        PopulateDbAsync(MyMusicPlayerRoomDatabase db) {
            musiqueDAO = db.musiqueDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //Ajout de musique
            musiqueDAO.deleteAll();
            Musique musique = new Musique("Slipknot", "The Devil In I", "The Grey Chapter", 2014, "Metal");
            musiqueDAO.insert(musique);
            musique = new Musique("Slipknot", "Unsainted", "We Are Not Your Kind", 2019, "Metal");
            musiqueDAO.insert(musique);

            return null;
        }
    }

}
