package com.example.projetws.beans;

import java.io.Serializable;

public class Etudiant implements Serializable {
    private int id;
    private String nom;
    private String prenom;
    private String ville;
    private String sexe;
    private String image; // Ajout du champ pour l'image

    public Etudiant() {
    }

    public Etudiant(int id, String nom, String prenom, String ville, String sexe, String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.sexe = sexe;
        this.image = image; // Initialisation de l'image
    }

    // Getters et Setters pour tous les champs
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getImage() {
        return image; // Getter pour l'image
    }

    public void setImage(String image) {
        this.image = image; // Setter pour l'image
    }
}
