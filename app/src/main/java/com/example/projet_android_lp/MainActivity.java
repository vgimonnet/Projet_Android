package com.example.projet_android_lp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.projet_android_lp.Activities.AddMusicNewActivity;
import com.example.projet_android_lp.Decorations.VerticalSpaceItemDecoration;
import com.example.projet_android_lp.Models.Musique;
import com.example.projet_android_lp.Utils.MusicListAdapter;
import com.example.projet_android_lp.Utils.MyMusicPlayerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMusicNewActivity.class);
                startActivityForResult(intent, NEW_MYMUSICPLAYER_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMusic);
        final MusicListAdapter adapter = new MusicListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

        myMusicPlayerViewModel = ViewModelProviders.of(this).get(MyMusicPlayerViewModel.class);

        this.FillDataBaseWithApi();

        myMusicPlayerViewModel.getAllMusiques().observe(this, new Observer<List<Musique>>() {
            @Override
            public void onChanged(@Nullable final List<Musique> musiques) {
                adapter.setMusiques(musiques);
                TextView textView = findViewById(R.id.txtBoxNbMusique);
                textView.setText("Nombre de musiques : "+musiques.size());
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

        if (requestCode == NEW_MYMUSICPLAYER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Musique musique = new Musique(data.getStringExtra("1"),
                    data.getStringExtra("2"),
                    data.getStringExtra("3"),
                    Integer.parseInt(data.getStringExtra("4")),
                    data.getStringExtra("5"));
            myMusicPlayerViewModel.insertMusique(musique);
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
            String urlString = baseUrl + URLEncoder.encode("Slipknot", "utf8") + "&api_key=" + getResources().getString(R.string.key) + "&format=json";
            return new URL(urlString);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * GetWeatherTask
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
                String artiste = "";
                String titre = "";
                if (nbTrackFind != 0) {
                    for (int i = 0; i < nbTrackFind; i++){
                        artiste = this.getArtiste(tracks, i);
                        titre = this.getTitre(tracks, i);
                        try {
                            Musique musique = new Musique(artiste.toString(),
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
                if (nbTotal> 0 && nbTotal<=30){
                    return nbTotal;
                }else if (nbTotal>30){
                    return 30;
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


}
