package com.test.actionzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

public class ParametresActivity extends AppCompatActivity {

    private EditText etNom, etThematique;
    private SwitchCompat switchNotifications;
    private CheckBox checkBoxAdulte;
    private RadioGroup radioGroupLangue;
    private Button btnSauvegarder;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etNom = findViewById(R.id.etNom);
        etThematique = findViewById(R.id.etThematique);
        switchNotifications = findViewById(R.id.switchNotifications);

        radioGroupLangue = findViewById(R.id.radioGroupLangue);
        btnSauvegarder = findViewById(R.id.btnSauvegarder);

        prefs = getSharedPreferences("parametres", MODE_PRIVATE);


        etNom.setText(prefs.getString("nom", ""));
        etThematique.setText(prefs.getString("thematique", ""));
        switchNotifications.setChecked(prefs.getBoolean("notifications", false));
        checkBoxAdulte.setChecked(prefs.getBoolean("adulte", false));

        String langue = prefs.getString("langue", "fr");
        if (langue.equals("fr")) {
            radioGroupLangue.check(R.id.radioFr);
        } else {
            radioGroupLangue.check(R.id.radioEn);
        }

        btnSauvegarder.setOnClickListener(v -> {
            String langueChoisie = "fr";
            if (radioGroupLangue.getCheckedRadioButtonId() == R.id.radioEn) {
                langueChoisie = "en";
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nom", etNom.getText().toString().trim());
            editor.putString("thematique", etThematique.getText().toString().trim());
            editor.putBoolean("notifications", switchNotifications.isChecked());
            editor.putBoolean("adulte", checkBoxAdulte.isChecked());
            editor.putString("langue", langueChoisie);
            editor.apply();

            Toast.makeText(this, "Paramètres sauvegardés !", Toast.LENGTH_SHORT).show();
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