package com.example.projet_android_lp.Utils;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projet_android_lp.Models.Musique;

import java.util.List;

public class MyMusicPlayerViewModel  extends AndroidViewModel {
    private MyMusicPlayerRepository mRepository;

    private LiveData<List<Musique>> allMusiques;
    private LiveData<Integer> nbMusiques;

    public MyMusicPlayerViewModel (Application application) {
        super(application);
        mRepository = new MyMusicPlayerRepository(application);
        allMusiques = mRepository.getAllMusiques();
        nbMusiques = mRepository.getNombreMusiques();
    }

    public LiveData<List<Musique>> getAllMusiques() { return allMusiques; }
    public LiveData<Integer> getNbMusique(){ return nbMusiques; }
    public void insertMusique(Musique musique) { mRepository.insertMusique(musique); }
    public void deleteAllMusiques(){ mRepository.deleteAllMusiques(); }
    public Integer nbMusiques(){ return mRepository.getNbMusiques();}

}
