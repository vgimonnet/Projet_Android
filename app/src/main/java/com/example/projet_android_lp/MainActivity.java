package com.example.projet_android_lp;

import android.content.Intent;
import android.os.Bundle;

import com.example.projet_android_lp.Activities.AddMusicNewActivity;
import com.example.projet_android_lp.Models.Musique;
import com.example.projet_android_lp.Utils.MusicListAdapter;
import com.example.projet_android_lp.Utils.MyMusicPlayerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        myMusicPlayerViewModel = ViewModelProviders.of(this).get(MyMusicPlayerViewModel.class);

        myMusicPlayerViewModel.getAllMusiques().observe(this, new Observer<List<Musique>>() {
            @Override
            public void onChanged(@Nullable final List<Musique> musiques) {
                // Update the cached copy of the words in the adapter.
                adapter.setMusiques(musiques);
                TextView textView = findViewById(R.id.textView3);
                textView.setText("nb elements via getAllWords : "+musiques.size());
            }
        });
        myMusicPlayerViewModel.getNbMusique().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                TextView textView = findViewById(R.id.textView4);
                textView.setText("nb elements via observer : "+integer);
            }
        });

        this.FillDataBaseWithApi();
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

    public void nbElements(View view){
        TextView textView = findViewById(R.id.textView2);
        textView.setText(myMusicPlayerViewModel.nbMusiques().toString());
    }

    public void FillDataBaseWithApi(){

    }
}
