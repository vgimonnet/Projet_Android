package com.example.projet_android_lp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_android_lp.R;

public class AddMusicNewActivity extends AppCompatActivity {
    //public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText txtBoxTitre;
    private EditText txtBoxArtiste;
    private EditText txtBoxAlbum;
    private EditText txtBoxAnnee;
    private Spinner spinnerGenre;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_music);
        txtBoxTitre = findViewById(R.id.txtBoxtitre);
        txtBoxArtiste = findViewById(R.id.txtBoxArtiste);
        txtBoxAlbum = findViewById(R.id.txtBoxAlbum);
        txtBoxAnnee = findViewById(R.id.txtBoxAnnee);
        spinnerGenre = findViewById(R.id.spinnerGenre);

        Spinner spinner = findViewById(R.id.spinnerGenre);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genres_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Button button = findViewById(R.id.btAjouter);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(txtBoxTitre.getText().toString()) || TextUtils.isEmpty(txtBoxArtiste.getText().toString()) || TextUtils.isEmpty(txtBoxAnnee.getText().toString()) || TextUtils.isEmpty(txtBoxAlbum.getText().toString())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String artiste = txtBoxArtiste.getText().toString();
                    String titre = txtBoxTitre.getText().toString();
                    String album = txtBoxAlbum.getText().toString();
                    String annee = txtBoxAnnee.getText().toString();
                    String genre = spinnerGenre.getSelectedItem().toString();

                    replyIntent.putExtra("1", artiste);
                    replyIntent.putExtra("2", titre);
                    replyIntent.putExtra("3", album);
                    replyIntent.putExtra("4", annee);
                    replyIntent.putExtra("5", genre);

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

        final Button btnClose = findViewById(R.id.btAnnuler);
        btnClose.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {finish();}
        });
    }
}
