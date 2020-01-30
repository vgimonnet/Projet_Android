package com.example.projet_android_lp.Utils;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.ArtisteWithMusiques;
import com.example.projet_android_lp.Models.Musique;

import java.util.List;

public class MyMusicPlayerViewModel  extends AndroidViewModel {
    private MyMusicPlayerRepository mRepository;

    private LiveData<List<Musique>> allMusiques;
    private LiveData<Integer> nbMusiques;

    private LiveData<List<Artiste>> allArtistes;
    private LiveData<Integer> nbArtistes;

    private LiveData<List<ArtisteWithMusiques>> artisteWithMusiques;

    public MyMusicPlayerViewModel (Application application) {
        super(application);
        mRepository = new MyMusicPlayerRepository(application);
        allMusiques = mRepository.getAllMusiques();
        nbMusiques = mRepository.getNombreMusiques();
        allArtistes = mRepository.getAllArtistes();
        nbArtistes = mRepository.getNombreArtiste();
        artisteWithMusiques = mRepository.getArtisteWithMusiques();
    }

    public LiveData<List<Musique>> getAllMusiques() { return allMusiques; }
    public LiveData<Integer> getNbMusique(){ return nbMusiques; }
    public void insertMusique(Musique musique) { mRepository.insertMusique(musique); }
    public void deleteAllMusiques(){ mRepository.deleteAllMusiques(); }
    public Integer nbMusiques(){ return mRepository.getNbMusiques();}

    public LiveData<List<Artiste>> getAllArtistes() { return allArtistes; }
    public LiveData<Integer> getNbArtistes(){ return nbArtistes; }
    public void insertArtiste(Artiste artiste) { mRepository.insertArtiste(artiste); }
    public void deleteAllArtistes(){ mRepository.deleteAllArtistes(); }
    public Integer nbArtistes(){ return mRepository.getNbArtiste();}

    public LiveData<List<ArtisteWithMusiques>> getArtisteWithMusiques() { return artisteWithMusiques; }
    public Artiste getArtisteById(long id){return mRepository.getArtisteById(id);}
    public Artiste getArtisteByName(String name){return mRepository.getArtisteByName(name);}

}
