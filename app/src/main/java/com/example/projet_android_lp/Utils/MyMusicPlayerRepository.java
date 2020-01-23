package com.example.projet_android_lp.Utils;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.projet_android_lp.Models.Musique;

import java.util.List;

public class MyMusicPlayerRepository {

    private MusiqueDAO musiqueDAO;

    private LiveData<List<Musique>> allMusiques;
    private LiveData<Integer> nbMusiques;

    MyMusicPlayerRepository(Application application) {
        MyMusicPlayerRoomDatabase db = MyMusicPlayerRoomDatabase.getDatabase(application);
        musiqueDAO = db.musiqueDAO();
        allMusiques = musiqueDAO.getAllMusiques();
        nbMusiques = musiqueDAO.nbElementsLD();
    }

    LiveData<List<Musique>> getAllMusiques() {
        return allMusiques;
    }
    LiveData<Integer> getNombreMusiques() { return nbMusiques; }

    //Méthodes pour les musiques

    public void insertMusique (Musique musique) {
        new insertMusiqueAsyncTask(musiqueDAO).execute(musique);
    }

    private static class insertMusiqueAsyncTask extends AsyncTask<Musique, Void, Void> {

        private MusiqueDAO mAsyncTaskDao;

        insertMusiqueAsyncTask(MusiqueDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Musique... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    public void deleteAllMusiques(){
        new deleteAllMusiquesAsyncTask(musiqueDAO).execute();
    }

    private static class deleteAllMusiquesAsyncTask extends AsyncTask<Void,Void,Void> {
        private MusiqueDAO mAsyncTaskDao;

        deleteAllMusiquesAsyncTask(MusiqueDAO dao){ mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(Void... params){
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


    public Integer getNbMusiques(){
        try {
            return new getNbMusiquesAsync(musiqueDAO).execute().get();
        }catch (Exception e) {
            Log.d("test", "pb getNbMusiques");
        }
        return null;
    }

    private static class getNbMusiquesAsync extends AsyncTask<Void,Void,Integer>{
        private MusiqueDAO mAsyncTaskDao;

        getNbMusiquesAsync(MusiqueDAO dao){mAsyncTaskDao = dao;}

        @Override
        protected Integer doInBackground(Void... params){
            return  new Integer(mAsyncTaskDao.nbElements());
        }
    }

}
