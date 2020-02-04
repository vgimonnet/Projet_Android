package com.example.projet_android_lp.Activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.example.projet_android_lp.Models.ArtisteWithMusiques;
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
    private List<ArtisteWithMusiques> artisteWithMusiquesList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_artistes);

        recyclerView = findViewById(R.id.recyclerViewArtiste);
        adapter = new ArtisteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Artiste currentArtiste = adapter.getCurrentArtiste(view, position);
                        if(currentArtiste != null){
                            openPopUpDetailMusique(view, currentArtiste);
                        }else{
                            Toast.makeText(getApplicationContext(), "Aucune information pour cette musique", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Artiste currentArtiste = adapter.getCurrentArtiste(view, position);
                        if(currentArtiste != null){
                            openPopUpDetailMusique(view, currentArtiste);
                        }else{
                            Toast.makeText(getApplicationContext(), "Aucune information pour cette musique", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );

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

        myMusicPlayerViewModel.getArtisteWithMusiquesLD().observe(this, new Observer<List<ArtisteWithMusiques>>() {
            @Override
            public void onChanged(@Nullable final List<ArtisteWithMusiques> artisteWithMusiques) {
                adapter.setArtisteWithMusiquesList(artisteWithMusiques);
                if (artisteWithMusiquesList != null){
                    artisteWithMusiquesList.clear();
                    artisteWithMusiquesList.addAll(artisteWithMusiques);
                }

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

    public void openPopUpDetailMusique(View view, final Artiste artiste){

        LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = mInflater.inflate(R.layout.popup_window_delete_artist, null);

        TextView lblMessage = popupView.findViewById(R.id.lblMessage);

        ImageButton closeButton = popupView.findViewById(R.id.ib_closeDelete);
        Button btAnnuler = popupView.findViewById(R.id.btAnnulerDelete);
        Button btConfirmer = popupView.findViewById(R.id.btConfirmerDelete);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.rgb(179, 217, 255)));

        String nomArtiste = artiste.getNom();

        final List<ArtisteWithMusiques> liste = myMusicPlayerViewModel.getArtisteWithMusiques();
        Integer nbmusiques = 0;

        for (ArtisteWithMusiques a : liste) {
            if (a.artiste.getArtisteId() == artiste.getArtisteId()){
                nbmusiques = a.musiques.size();
            }
        }

        lblMessage.setText("Voulez-vous vraiment supprimer l'artiste " + nomArtiste + " ainsi que toutes ces musiques, soit " + Integer.toString(nbmusiques) + " musiques.");

        btAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        btConfirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ArtisteWithMusiques a: liste) {
                    if (a.artiste.getArtisteId() == artiste.getArtisteId()){
                        for (Musique m: a.musiques) {
                            myMusicPlayerViewModel.deleteMusique(m);
                        }
                    }
                }
                myMusicPlayerViewModel.deleteArtiste(artiste);
                popupWindow.dismiss();
            }
        });

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
