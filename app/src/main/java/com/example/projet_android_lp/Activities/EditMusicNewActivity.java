package com.example.projet_android_lp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_android_lp.R;

public class EditMusicNewActivity extends AppCompatActivity {
    private EditText txtBoxTitre;
    private EditText txtBoxArtiste;
    private EditText txtBoxAlbum;
    private EditText txtBoxAnnee;
    private Spinner spinner;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_music);

        txtBoxTitre = findViewById(R.id.txtBoxtitreEdit);
        txtBoxArtiste = findViewById(R.id.txtBoxArtisteEdit);
        txtBoxAlbum = findViewById(R.id.txtBoxAlbumEdit);
        txtBoxAnnee = findViewById(R.id.txtBoxAnneeEdit);

        spinner = findViewById(R.id.spinnerGenreEdit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genres_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Intent intent = getIntent();
        id = null;

        if (intent != null){
            String genre = intent.getStringExtra("genre");
            id = intent.getStringExtra("id");

            txtBoxTitre.setText(intent.getStringExtra("titre"));
            txtBoxArtiste.setText(intent.getStringExtra("artiste"));
            txtBoxAlbum.setText(intent.getStringExtra("album"));
            txtBoxAnnee.setText(intent.getStringExtra("annee"));
            if (genre != null){
                spinner.setSelection(((ArrayAdapter)spinner.getAdapter()).getPosition(genre));
            }
        }

        final Button button = findViewById(R.id.btAjouterEdit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(txtBoxTitre.getText().toString()) || TextUtils.isEmpty(txtBoxArtiste.getText().toString()) ) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String artiste = txtBoxArtiste.getText().toString();
                    String titre = txtBoxTitre.getText().toString();
                    String album = txtBoxAlbum.getText().toString();
                    String annee = txtBoxAnnee.getText().toString();
                    String genre = spinner.getSelectedItem().toString();

                    replyIntent.putExtra("1", artiste);
                    replyIntent.putExtra("2", titre);
                    replyIntent.putExtra("3", album);
                    replyIntent.putExtra("4", annee);
                    replyIntent.putExtra("5", genre);
                    replyIntent.putExtra("6", id);

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

        final Button btnClose = findViewById(R.id.btAnnulerEdit);
        btnClose.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {finish();}
        });
    }
}
