package com.example.projet_android_lp.Utils;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.ArtisteWithMusiques;
import com.example.projet_android_lp.Models.Musique;

import java.util.List;

public class MyMusicPlayerRepository {

    private MusiqueDAO musiqueDAO;
    private ArtisteDAO artisteDAO;

    private LiveData<List<Musique>> allMusiques;
    private LiveData<Integer> nbMusiques;
    private LiveData<List<Artiste>> allArtistes;
    private LiveData<Integer> nbArtistes;
    private LiveData<List<ArtisteWithMusiques>> artisteWithMusiques;

    MyMusicPlayerRepository(Application application) {
        MyMusicPlayerRoomDatabase db = MyMusicPlayerRoomDatabase.getDatabase(application);
        musiqueDAO = db.musiqueDAO();
        artisteDAO = db.artisteDAO();
        allMusiques = musiqueDAO.getAllMusiques();
        nbMusiques = musiqueDAO.nbElementsLD();
        allArtistes = artisteDAO.getAllArtistes();
        nbArtistes = artisteDAO.nbArtisteLD();
        artisteWithMusiques = musiqueDAO.getArtisteWithPlaylistsLD();
    }

    LiveData<List<Musique>> getAllMusiques() {
        return allMusiques;
    }
    LiveData<Integer> getNombreMusiques() { return nbMusiques; }

    LiveData<List<Artiste>> getAllArtistes() {
        return allArtistes;
    }
    LiveData<Integer> getNombreArtiste() { return nbArtistes; }

    LiveData<List<ArtisteWithMusiques>> getArtisteWithMusiques() { return artisteWithMusiques;}


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

    public void deleteMusique (Musique musique) {
        new deleteMusiqueAsyncTask(musiqueDAO).execute(musique);
    }

    private static class deleteMusiqueAsyncTask extends AsyncTask<Musique, Void, Void> {

        private MusiqueDAO mAsyncTaskDao;

        deleteMusiqueAsyncTask(MusiqueDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Musique... params) {
            mAsyncTaskDao.delete(params[0]);
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


    //Méthodes pour les Artistes

    public void insertArtiste (Artiste artiste) {
        new insertArtisteAsyncTask(artisteDAO).execute(artiste);
    }

    private static class insertArtisteAsyncTask extends AsyncTask<Artiste, Void, Void> {

        private ArtisteDAO mAsyncTaskDao;

        insertArtisteAsyncTask(ArtisteDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Artiste... params) {
            mAsyncTaskDao.insertArtiste(params[0]);
            return null;
        }
    }


    public void deleteAllArtistes(){
        new deleteAllArtistesAsyncTask(artisteDAO).execute();
    }

    private static class deleteAllArtistesAsyncTask extends AsyncTask<Void,Void,Void> {
        private ArtisteDAO mAsyncTaskDao;

        deleteAllArtistesAsyncTask(ArtisteDAO dao){ mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(Void... params){
            mAsyncTaskDao.deleteAllArtiste();
            return null;
        }
    }


    public Integer getNbArtiste(){
        try {
            return new getNbArtistesAsync(artisteDAO).execute().get();
        }catch (Exception e) {
            Log.d("test", "pb getNbArtistes");
        }
        return null;
    }

    private static class getNbArtistesAsync extends AsyncTask<Void,Void,Integer>{
        private ArtisteDAO mAsyncTaskDao;

        getNbArtistesAsync(ArtisteDAO dao){mAsyncTaskDao = dao;}

        @Override
        protected Integer doInBackground(Void... params){
            return  new Integer(mAsyncTaskDao.nbArtiste());
        }
    }


    public Artiste getArtisteById(Long id){
        try {
            return new getArtisteByIdAsync(artisteDAO).execute(id).get();
        }catch (Exception e) {
            Log.d("MesLogs", "pb getArtisteById");
        }
        return null;
    }

    private static class getArtisteByIdAsync extends AsyncTask<Long, Void, Artiste>{
        private ArtisteDAO mAsyncTaskDao;

        getArtisteByIdAsync(ArtisteDAO dao){mAsyncTaskDao = dao;}

        @Override
        protected Artiste doInBackground(Long... params){
            return mAsyncTaskDao.getArtisteById(params[0]);
        }
    }


    public Artiste getArtisteByName(String name){
        try {
            return new getArtisteByNameAsync(artisteDAO).execute(name).get();
        }catch (Exception e){
            Log.d("MesLogs", "pb getArtisteByName");
        }
        return null;
    }

    private static class getArtisteByNameAsync extends AsyncTask<String, Void, Artiste>{
        private ArtisteDAO mAsyncTaskDao;

        getArtisteByNameAsync(ArtisteDAO dao){mAsyncTaskDao = dao;}

        @Override
        protected Artiste doInBackground(String... params){
            return mAsyncTaskDao.getArtisteByNom(params[0]);
        }
    }
}
