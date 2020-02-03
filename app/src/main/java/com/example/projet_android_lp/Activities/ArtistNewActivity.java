package com.example.projet_android_lp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_android_lp.Decorations.VerticalSpaceItemDecoration;
import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.Musique;
import com.example.projet_android_lp.R;
import com.example.projet_android_lp.Utils.ArtisteListAdapter;
import com.example.projet_android_lp.Utils.MusicListAdapter;
import com.example.projet_android_lp.Utils.MyMusicPlayerViewModel;
import com.example.projet_android_lp.Utils.RecyclerItemClickListener;

import java.util.List;

public class ArtistNewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArtisteListAdapter adapter;
    private MyMusicPlayerViewModel myMusicPlayerViewModel;
    private static final int VERTICAL_ITEM_SPACE = 48;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_artistes);

        recyclerView = findViewById(R.id.recyclerViewArtiste);
        adapter = new ArtisteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Musique currentMusique = adapter.getCurrentMusique(view, position);
                        if(currentMusique != null){
                            openPopUpDetailMusique(view, currentMusique);
                        }else{
                            Toast.makeText(getApplicationContext(), "Aucune information pour cette musique", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Musique currentMusique = adapter.getCurrentMusique(view, position);
                        if(currentMusique != null){
                            openPopUpDetailMusique(view, currentMusique);
                        }else{
                            Toast.makeText(getApplicationContext(), "Aucune information pour cette musique", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );*/

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        myMusicPlayerViewModel = ViewModelProviders.of(this).get(MyMusicPlayerViewModel.class);

        myMusicPlayerViewModel.getAllArtistes().observe(this, new Observer<List<Artiste>>() {
            @Override
            public void onChanged(List<Artiste> artistes) {
                adapter.setArtistes(artistes);
            }
        });

        myMusicPlayerViewModel.getAllMusiques().observe(this, new Observer<List<Musique>>() {
            @Override
            public void onChanged(@Nullable final List<Musique> musiques) {
                adapter.setMusiques(musiques);
            }
        });

        //Mise en place de la recherche par titre
        searchView = findViewById(R.id.searchViewArtisteMenu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filterArtiste(getApplicationContext(), query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterArtiste(getApplicationContext() ,newText);
                return true;
            }
        });

        final Button btnClose = findViewById(R.id.bt_retour_artistemenu);
        btnClose.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {finish();}
        });

    }
}
