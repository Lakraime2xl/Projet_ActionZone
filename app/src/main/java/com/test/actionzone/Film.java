package com.test.actionzone;

public class Film {


    String titre;
    String synopsis;
    String affiche;
    double note;


    String statut;
    float maNote;
    String avis;


    public Film(String titre, String synopsis, String affiche, double note) {
        this.titre = titre;
        this.synopsis = synopsis;
        this.affiche = affiche;
        this.note = note;


        this.statut = "A voir";
        this.maNote = 0;
        this.avis = "";
    }
}