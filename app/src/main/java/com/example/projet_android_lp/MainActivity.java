package com.example.projet_android_lp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.projet_android_lp.Activities.AddMusicNewActivity;
import com.example.projet_android_lp.Activities.EditMusicNewActivity;
import com.example.projet_android_lp.Decorations.SwipeToDeleteCallback;
import com.example.projet_android_lp.Decorations.SwipeToEditCallback;
import com.example.projet_android_lp.Decorations.VerticalSpaceItemDecoration;
import com.example.projet_android_lp.Models.Artiste;
import com.example.projet_android_lp.Models.Musique;
import com.example.projet_android_lp.Utils.MusicListAdapter;
import com.example.projet_android_lp.Utils.MyMusicPlayerViewModel;
import com.example.projet_android_lp.Utils.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int VERTICAL_ITEM_SPACE = 48;
    private MyMusicPlayerViewModel myMusicPlayerViewModel;
    public static final int NEW_MYMUSICPLAYER_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_MYMUSICPLAYER_ACTIVITY_REQUEST_CODE = 2;
    public ConstraintLayout constraintLayout;
    public MusicListAdapter adapter;
    public RecyclerView recyclerView;
    public SearchView searchViewTitre;
    public SearchView searchViewArtiste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        constraintLayout = findViewById(R.id.idForSwipeDelete);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMusicNewActivity.class);
                startActivityForResult(intent, NEW_MYMUSICPLAYER_ACTIVITY_REQUEST_CODE);
            }
        });


        recyclerView = findViewById(R.id.recyclerViewMusic);
        //final MusicListAdapter adapter = new MusicListAdapter(this);
        adapter = new MusicListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
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
        );

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        myMusicPlayerViewModel = ViewModelProviders.of(this).get(MyMusicPlayerViewModel.class);



        myMusicPlayerViewModel.getAllMusiques().observe(this, new Observer<List<Musique>>() {
            @Override
            public void onChanged(@Nullable final List<Musique> musiques) {
                adapter.setMusiques(musiques);
                TextView textView = findViewById(R.id.txtBoxNbMusique);
                textView.setText("Nombre de musiques : " + musiques.size());
            }
        });

        myMusicPlayerViewModel.getAllArtistes().observe(this, new Observer<List<Artiste>>() {
            @Override
            public void onChanged(List<Artiste> artistes) {
                adapter.setArtistes(artistes);
            }
        });

        this.FillDataBaseWithApi();

        this.enableSwipeToEdit();
        this.enableSwipeToDeleteAndUndo();


        //Mise en place de la recherche par titre
        searchViewTitre = findViewById(R.id.searchViewTitre);
        searchViewTitre.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filterMusique(getApplicationContext(), query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterMusique(getApplicationContext() ,newText);
                return true;
            }
        });

        //Mise en place de la recherche par artiste
        searchViewArtiste = findViewById(R.id.searchViewArtiste);
        searchViewArtiste.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("test", Integer.toString(requestCode));

        if (requestCode == NEW_MYMUSICPLAYER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Long idArtiste = null;
            String nomArtiste = data.getStringExtra("1");
            String titre = data.getStringExtra("2");
            String album = data.getStringExtra("3");
            int annee = Integer.parseInt(data.getStringExtra("4"));
            String genre = data.getStringExtra("5");

            if(nomArtiste != null && titre != null){
                idArtiste = getArtisteIdOrCreateIt(nomArtiste);
                if(idArtiste != null){
                    Musique musique = new Musique(idArtiste,titre, album, annee, genre);
                    myMusicPlayerViewModel.insertMusique(musique);
                }
            }
        } else if (requestCode == EDIT_MYMUSICPLAYER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Long idArtiste = null;
            Long idMusique = Long.parseLong(data.getStringExtra("6"));
            Musique musiqueToEdit = myMusicPlayerViewModel.getMusiqueById(idMusique);

            if (musiqueToEdit != null) {
                String nomArtiste = data.getStringExtra("1");
                String titre = data.getStringExtra("2");
                String album = data.getStringExtra("3");
                int annee = Integer.parseInt(data.getStringExtra("4"));
                String genre = data.getStringExtra("5");

                if(nomArtiste != null && titre != null){
                    idArtiste = getArtisteIdOrCreateIt(nomArtiste);
                    if(idArtiste != null){
                        //Musique musique = new Musique(idArtiste,titre, album, annee, genre);
                        musiqueToEdit.setTitre(titre);
                        musiqueToEdit.setAlbum(album);
                        musiqueToEdit.setAnnee(annee);
                        musiqueToEdit.setGenre(genre);
                        musiqueToEdit.setArtisteRefId(idArtiste);
                        myMusicPlayerViewModel.updateMusique(musiqueToEdit);
                    }
                }
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void deleteAll(View view){
        myMusicPlayerViewModel.deleteAllMusiques();
    }

    public void FillDataBaseWithApi(){
        URL url = this.createUrl();
        new GetTrackTask(this).execute(url);

    }

    private URL createUrl(){
        String baseUrl = "http://ws.audioscrobbler.com/2.0/?method=track.search&track=";
        try {
            String urlString = baseUrl + URLEncoder.encode("System-Of-A-Down", "utf8") + "&api_key=" + getResources().getString(R.string.key) + "&format=json";
            return new URL(urlString);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * GetTrackTask
     */
    private class GetTrackTask extends AsyncTask<URL, Void, JSONObject> {
        private Context context;

        public GetTrackTask(Context context) {
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                connection.setConnectTimeout(5000);
                Integer reponse = connection.getResponseCode();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    )) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    }
                    catch (Exception e) {
                        Log.d("MesLogs", "pb connexion");
                    }

                    return new JSONObject(builder.toString());
                }
            }
            catch (Exception e) {
                Log.d("MesLogs", "Pb connexion");
            }
            finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject tracks) {
            if (tracks != null) {
                Integer nbTrackFind = this.getNbTrackFind(tracks);
                String titre = "";
                Long idArtiste = null;
                if (nbTrackFind != 0) {
                    for (int i = 0; i < nbTrackFind; i++){
                        idArtiste = getArtisteIdOrCreateIt(this.getArtiste(tracks, i));
                        titre = this.getTitre(tracks, i);
                        try {
                            Musique musique = new Musique(idArtiste,
                                    titre.toString(),
                                    "none",
                                    0,
                                    "none");
                            myMusicPlayerViewModel.insertMusique(musique);
                        }catch (Exception e){
                            Log.d("MesLogs", "Erreur Insertion Musique dans DB");
                        }
                    }
                }
            }
        }

        /**
         * getNbTrackFind
         * @param object
         * @return
         */
        private Integer getNbTrackFind(JSONObject object){
            try {
                JSONObject results = object.getJSONObject("results");
                Integer nbTotal = results.getInt("opensearch:totalResults");
                if (nbTotal> 0 && nbTotal<=5){
                    return nbTotal;
                }else if (nbTotal>5){
                    return 5;
                }else{
                    return 0;
                }
            }catch (Exception e){
                Log.d("MesLogs", "Pb JSON");
            }
            return null;
        }

        /**
         * getArtiste
         * @param object
         * @return
         */
        private String getArtiste(JSONObject object, Integer id) {
            try {
                JSONArray results = object.getJSONObject("results").getJSONObject("trackmatches").getJSONArray("track");
                JSONObject track = results.getJSONObject(id);
                return track.getString("artist");
            }
            catch (Exception e) {
                Log.d("MesLogs", "pb JSON");
            }
            return null;
        }

        /**
         * getTitre
         * @param object
         * @return
         */
        private String getTitre(JSONObject object, Integer id) {
            try {
                JSONArray results = object.getJSONObject("results").getJSONObject("trackmatches").getJSONArray("track");
                JSONObject track = results.getJSONObject(id);
                return track.getString("name");
            }
            catch (Exception e) {
                Log.d("MesLogs", "pb JSON");
            }
            return null;
        }
    }


    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Musique musique = adapter.getData().get(position);

                adapter.removeItem(position);
                myMusicPlayerViewModel.deleteMusique(musique);


                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "La musique a été supprimé de la liste..", Snackbar.LENGTH_LONG);
                snackbar.setAction("ANNULER", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adapter.restoreItem(musique, position);
                        recyclerView.scrollToPosition(position);
                        myMusicPlayerViewModel.insertMusique(musique);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void enableSwipeToEdit() {
        SwipeToEditCallback swipeToEditCallback= new SwipeToEditCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Musique musique = adapter.getData().get(position);

                Long idArtiste = musique.getArtisteRefId() + 1;

                Artiste artiste = myMusicPlayerViewModel.getArtisteById(idArtiste);

                Intent intent = new Intent(MainActivity.this, EditMusicNewActivity.class);
                intent.putExtra("id", Long.toString(musique.getId()));
                intent.putExtra("titre", musique.getTitre());
                intent.putExtra("artiste", artiste.getNom());
                intent.putExtra("album", musique.getAlbum());
                intent.putExtra("annee", Integer.toString(musique.getAnnee()));
                intent.putExtra("genre", musique.getGenre());
                startActivityForResult(intent, EDIT_MYMUSICPLAYER_ACTIVITY_REQUEST_CODE);

                adapter.removeItem(position);
                adapter.restoreItem(musique, position);
                recyclerView.scrollToPosition(position);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToEditCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public long getArtisteIdOrCreateIt(String nomArtiste){
        Long idArtiste = null;
        Artiste artiste = myMusicPlayerViewModel.getArtisteByName(nomArtiste);
        if(artiste != null){
            idArtiste = artiste.getArtisteId();
        }else{
            artiste = new Artiste(nomArtiste);
            myMusicPlayerViewModel.insertArtiste(artiste);
            artiste = myMusicPlayerViewModel.getArtisteByName(nomArtiste);
            idArtiste = artiste.getArtisteId();
        }
        return idArtiste-1;
    }

    public void openPopUpDetailMusique(View view, Musique musique){

        LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = mInflater.inflate(R.layout.popup_window, null);

        TextView lbltitre = popupView.findViewById(R.id.lblTitreInfoContenu);
        TextView lblartiste = popupView.findViewById(R.id.lblArtisteInfoContenu);
        TextView lblalbum = popupView.findViewById(R.id.lblAlbumInfoContenu);
        TextView lblannee = popupView.findViewById(R.id.lblAnneeInfoContenu);
        TextView lblgenre = popupView.findViewById(R.id.lblGenreInfoContenu);

        ImageButton closeButton = popupView.findViewById(R.id.ib_close);

        ;

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.rgb(179, 217, 255)));

        lbltitre.setText(musique.getTitre());
        Artiste artiste = myMusicPlayerViewModel.getArtisteById(musique.getArtisteRefId() + 1);
        if(artiste != null){
            lblartiste.setText(artiste.getNom());
        }else{
            lblartiste.setText("Artiste inconnu");
        }
        lblalbum.setText(musique.getAlbum());
        lblannee.setText(Integer.toString(musique.getAnnee()));
        lblgenre.setText(musique.getGenre());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
