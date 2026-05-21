package com.test.actionzone;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    private ImageView imgAffiche;
    private TextView tvTitre, tvNote, tvSynopsis, tvDateChoisie;
    private Spinner spinnerStatut;
    private RatingBar ratingBar;
    private EditText etAvis;
    private Button btnAjouter, btnDate;

    private Film film;

    // Pour sauvegarder la date choisie
    private String dateChoisie = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // On relie les éléments XML au Java
        imgAffiche = findViewById(R.id.imgAfficheDetail);
        tvTitre = findViewById(R.id.tvTitreDetail);
        tvNote = findViewById(R.id.tvNoteDetail);
        tvSynopsis = findViewById(R.id.tvSynopsisDetail);
        spinnerStatut = findViewById(R.id.spinnerStatut);
        ratingBar = findViewById(R.id.ratingBar);
        etAvis = findViewById(R.id.etAvis);
        btnAjouter = findViewById(R.id.btnAjouter);
        btnDate = findViewById(R.id.btnDate);
        tvDateChoisie = findViewById(R.id.tvDateChoisie);

        // On récupère les données envoyées depuis RechercheActivity
        String titre = getIntent().getStringExtra("titre");
        String synopsis = getIntent().getStringExtra("synopsis");
        String affiche = getIntent().getStringExtra("affiche");
        double note = getIntent().getDoubleExtra("note", 0);

        // On crée l'objet Film
        film = new Film(titre, synopsis, affiche, note);

        // On affiche les données du film
        tvTitre.setText(titre);
        tvNote.setText("Note TMDB : " + note);
        tvSynopsis.setText(synopsis);

        // On charge l'affiche avec Glide
        String urlAffiche = "https://image.tmdb.org/t/p/w500" + affiche;
        Glide.with(this).load(urlAffiche).into(imgAffiche);

        // On remplit le Spinner avec les statuts possibles
        String[] statuts = {"A voir", "En cours", "Vu"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuts
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatut.setAdapter(adapterSpinner);

        // Clic sur le bouton date : ouvre le DatePickerDialog
        btnDate.setOnClickListener(v -> {
            // On récupère la date d'aujourd'hui par défaut
            Calendar calendar = Calendar.getInstance();
            int annee = calendar.get(Calendar.YEAR);
            int mois = calendar.get(Calendar.MONTH);
            int jour = calendar.get(Calendar.DAY_OF_MONTH);

            // On crée le DatePickerDialog
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, anneeChoisie, moisChoisi, jourChoisi) -> {
                        // Quand l'utilisateur choisit une date
                        dateChoisie = jourChoisi + "/" + (moisChoisi + 1) + "/" + anneeChoisie;
                        tvDateChoisie.setText("Date : " + dateChoisie);
                    },
                    annee, mois, jour
            );
            dialog.show();
        });

        // Quand on clique sur Ajouter
        btnAjouter.setOnClickListener(v -> {
            film.statut = spinnerStatut.getSelectedItem().toString();
            film.maNote = ratingBar.getRating();
            film.avis = etAvis.getText().toString().trim();

            // On sauvegarde le film dans le fichier
            GestionFichier.ajouterFilm(this, film);

            Toast.makeText(this, film.titre + " ajouté à ta collection !", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // onSaveInstanceState : sauvegarde les données si l'app est interrompue
    // Par exemple si l'utilisateur tourne l'écran
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("dateChoisie", dateChoisie);
        outState.putFloat("maNote", ratingBar.getRating());
        outState.putString("avis", etAvis.getText().toString());
    }

    // onRestoreInstanceState : restaure les données sauvegardées
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dateChoisie = savedInstanceState.getString("dateChoisie", "");
        if (!dateChoisie.isEmpty()) {
            tvDateChoisie.setText("Date : " + dateChoisie);
        }
        ratingBar.setRating(savedInstanceState.getFloat("maNote", 0));
        etAvis.setText(savedInstanceState.getString("avis", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}