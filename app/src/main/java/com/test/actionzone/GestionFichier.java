package com.test.actionzone;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GestionFichier {


    private static final String NOM_FICHIER = "collection.json";


    public static void ajouterFilm(Context context, Film film) {
        try {

            ArrayList<Film> liste = lireFilms(context);


            liste.add(film);


            JSONArray jsonArray = new JSONArray();
            for (Film f : liste) {
                JSONObject obj = new JSONObject();
                obj.put("titre", f.titre);
                obj.put("synopsis", f.synopsis);
                obj.put("affiche", f.affiche);
                obj.put("note", f.note);
                obj.put("statut", f.statut);
                obj.put("maNote", f.maNote);
                obj.put("avis", f.avis);
                jsonArray.put(obj);
            }


            FileOutputStream fos = context.openFileOutput(NOM_FICHIER, Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Film> lireFilms(Context context) {
        ArrayList<Film> liste = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(NOM_FICHIER);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder contenu = new StringBuilder();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                contenu.append(ligne);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(contenu.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Film film = new Film(
                        obj.getString("titre"),
                        obj.getString("synopsis"),
                        obj.getString("affiche"),
                        obj.getDouble("note")
                );
                film.statut = obj.optString("statut", "A voir");
                film.maNote = (float) obj.optDouble("maNote", 0);
                film.avis = obj.optString("avis", "");
                liste.add(film);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }


    public static void supprimerFilm(Context context, int position) {
        try {
            ArrayList<Film> liste = lireFilms(context);
            liste.remove(position);

            JSONArray jsonArray = new JSONArray();
            for (Film f : liste) {
                JSONObject obj = new JSONObject();
                obj.put("titre", f.titre);
                obj.put("synopsis", f.synopsis);
                obj.put("affiche", f.affiche);
                obj.put("note", f.note);
                obj.put("statut", f.statut);
                obj.put("maNote", f.maNote);
                obj.put("avis", f.avis);
                jsonArray.put(obj);
            }

            FileOutputStream fos = context.openFileOutput(NOM_FICHIER, Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}