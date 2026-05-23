package com.test.actionzone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class RechercheActivity extends AppCompatActivity {

    private EditText etRecherche;
    private Button btnRechercher;
    private RecyclerView recyclerView;
    private FilmAdapter adapter;

    private ArrayList<Film> listeFilms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etRecherche = findViewById(R.id.etRecherche);
        btnRechercher = findViewById(R.id.btnRechercher);
        recyclerView = findViewById(R.id.recyclerViewResultats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new FilmAdapter(this, listeFilms, film -> {
            Intent intent = new Intent(RechercheActivity.this, DetailActivity.class);
            intent.putExtra("titre", film.titre);
            intent.putExtra("synopsis", film.synopsis);
            intent.putExtra("affiche", film.affiche);
            intent.putExtra("note", film.note);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        btnRechercher.setOnClickListener(v -> {
            String query = etRecherche.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(this, "Tape un nom de film !", Toast.LENGTH_SHORT).show();
            } else {
                rechercherFilms(query);
            }
        });
    }

    private void rechercherFilms(String query) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String cleApi = "cbcd4a42f8d970c6629821446aac4caf";
                String urlString = "https://api.themoviedb.org/3/search/movie?api_key="
                        + cleApi + "&query=" + query + "&with_genres=28&language=fr-FR";

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder reponse = new StringBuilder();
                String ligne;
                while ((ligne = reader.readLine()) != null) {
                    reponse.append(ligne);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(reponse.toString());
                JSONArray results = jsonObject.getJSONArray("results");

                listeFilms.clear();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject film = results.getJSONObject(i);
                    String titre = film.getString("title");
                    String synopsis = film.optString("overview", "Pas de synopsis");
                    String affiche = film.optString("poster_path", "");
                    double note = film.optDouble("vote_average", 0);

                    listeFilms.add(new Film(titre, synopsis, affiche, note));
                }

                runOnUiThread(() -> {
                    if (listeFilms.isEmpty()) {
                        Toast.makeText(this, "Aucun résultat trouvé", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, listeFilms.size() + " films trouvés", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                );
            }
        });
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