package com.example.projetws;
import android.app.Activity; // Import Activity
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import android.util.Log;
import android.widget.EditText; // Ajoutez cette ligne pour l'EditText

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

import android.widget.Spinner;
import android.widget.RadioGroup;
import android.widget.RadioButton;

import android.widget.ImageView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import com.example.projetws.beans.Etudiant;

public class EtudiantAdapter extends ArrayAdapter<Etudiant> {

    private Context context;
    private List<Etudiant> etudiants;
    private String deleteUrl = "http://10.0.2.2/project/ws/deleteEtudiant.php"; // URL du script de suppression

    public EtudiantAdapter(Context context, List<Etudiant> etudiants) {
        super(context, 0, etudiants);
        this.context = context;
        this.etudiants = etudiants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Etudiant etudiant = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_etudiant, parent, false);
        }

        TextView textViewNom = convertView.findViewById(R.id.textViewNom);
        TextView textViewPrenom = convertView.findViewById(R.id.textViewPrenom);
        TextView textViewVille = convertView.findViewById(R.id.textViewVille);
        TextView textViewSexe = convertView.findViewById(R.id.textViewSexe);
        ImageView imageViewEtudiant = convertView.findViewById(R.id.imageViewEtudiant); // Ajout de l'ImageView
        Button buttonSupprimer = convertView.findViewById(R.id.buttonSupprimer);
        Button buttonModifier = convertView.findViewById(R.id.buttonModifier); // Nouveau bouton pour la modification

        // Afficher le nom et le prénom sans préfixes
        textViewNom.setText(etudiant.getNom());
        textViewPrenom.setText(etudiant.getPrenom());

        // Afficher la ville avec une icône de localisation
        textViewVille.setText(etudiant.getVille());
        // Ajout d'une icône de localisation avant le texte de la ville
        textViewVille.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_localisation, 0, 0, 0); // Remplacez 'ic_localisation' par votre icône

        // Afficher le sexe avec une icône correspondante
        if (etudiant.getSexe().equalsIgnoreCase("Homme")) {
            textViewSexe.setText("Homme");
            textViewSexe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_homme, 0, 0, 0); // Remplacez 'ic_homme' par votre icône
        } else {
            textViewSexe.setText("Femme");
            textViewSexe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_femme, 0, 0, 0); // Remplacez 'ic_femme' par votre icône
        }

        // Récupérer l'URL de l'image et l'afficher dans les logs
        String imageUrl = etudiant.getImage();
        Log.d("EtudiantAdapter", "Image URL: " + imageUrl); // Affichage de l'URL de l'image dans les logs

        String baseUrl = "http://10.0.2.2/project/";
        String relativeImageUrl = etudiant.getImage(); // "uploads/1729360622.jpg"
        String fullImageUrl = baseUrl + relativeImageUrl; // "http://10.0.2.2/project/uploads/1729360622.jpg"

        Glide.with(context)
                .load(fullImageUrl)
                .error(R.drawable.image_error) // image par défaut en cas d'erreur
                .into(imageViewEtudiant);

        // Gestion des boutons
        buttonSupprimer.setOnClickListener(v -> showConfirmationDialog(etudiant));
        buttonModifier.setOnClickListener(v -> showEditDialog(etudiant));

        return convertView;
    }



    private void showConfirmationDialog(Etudiant etudiant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmer la suppression");
        builder.setMessage("Êtes-vous sûr de vouloir supprimer cet étudiant ?");

        builder.setPositiveButton("Oui", (dialog, which) -> {
            deleteEtudiant(etudiant);
        });

        builder.setNegativeButton("Non", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteEtudiant(Etudiant etudiant) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                deleteUrl + "?id=" + etudiant.getId(), // Passer l'ID de l'étudiant dans l'URL
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
                        // Mettre à jour la liste des étudiants après la suppression
                        etudiants.remove(etudiant);
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(stringRequest);
    }

    private void showEditDialog(Etudiant etudiant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_edit_etudiant, null); // Assurez-vous que le layout est correct

        // Champs de texte pour modifier les informations de l'étudiant
        EditText editTextNom = view.findViewById(R.id.editTextNom);
        EditText editTextPrenom = view.findViewById(R.id.editTextPrenom);
        Spinner spinnerVille = view.findViewById(R.id.spinnerVille); // Utilisez le Spinner
        RadioGroup radioGroupSexe = view.findViewById(R.id.radioGroupSexe); // Utilisez le RadioGroup
        ImageView imageView = view.findViewById(R.id.imageView); // Récupérer l'ImageView

        // Ajoute ceci lorsque tu ouvres le dialogue de modification dans ton fragment
        Button buttonSelectImage = view.findViewById(R.id.selectImage); // Assure-toi d'avoir ce bouton dans ton layout

        buttonSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            // Utilisez le contexte pour démarrer l'activité
            ((Activity) context).startActivityForResult(intent, 1); // 1 est le code de requête
        });

        // Pré-remplir les champs avec les valeurs actuelles de l'étudiant
        editTextNom.setText(etudiant.getNom());
        editTextPrenom.setText(etudiant.getPrenom());

        // Configurer le Spinner avec les villes
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.villes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVille.setAdapter(adapter);

        // Sélectionner la ville actuelle
        int spinnerPosition = adapter.getPosition(etudiant.getVille());
        spinnerVille.setSelection(spinnerPosition);

        // Sélectionner le sexe actuel
        if (etudiant.getSexe().equals("homme")) {
            radioGroupSexe.check(R.id.m);
        } else {
            radioGroupSexe.check(R.id.f);
        }

        // Charger l'image de l'étudiant dans l'ImageView
        String imageUrl = etudiant.getImage();
        String baseUrl = "http://10.0.2.2/project/";
        String fullImageUrl = baseUrl + imageUrl;

        Glide.with(context)
                .load(fullImageUrl)
                .error(R.drawable.image_error) // image par défaut en cas d'erreur
                .into(imageView);

        builder.setView(view);
        builder.setTitle("Modifier l'étudiant");

        builder.setPositiveButton("Modifier", (dialog, which) -> {
            // Récupérer les nouvelles valeurs du formulaire
            String nouveauNom = editTextNom.getText().toString();
            String nouveauPrenom = editTextPrenom.getText().toString();
            String nouvelleVille = spinnerVille.getSelectedItem().toString(); // Récupérer la ville du Spinner

            // Récupérer le sexe sélectionné
            int selectedSexeId = radioGroupSexe.getCheckedRadioButtonId();
            String nouveauSexe;
            if (selectedSexeId == R.id.m) {
                nouveauSexe = "homme";
            } else {
                nouveauSexe = "femme";
            }

            // Mettre à jour l'objet étudiant
            etudiant.setNom(nouveauNom);
            etudiant.setPrenom(nouveauPrenom);
            etudiant.setVille(nouvelleVille);
            etudiant.setSexe(nouveauSexe);

            // Appeler la méthode pour envoyer la requête de mise à jour
            updateEtudiant(etudiant);
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void updateEtudiant(Etudiant etudiant) {
        String updateUrl = "http://10.0.2.2/project/ws/updateEtudiant.php"; // URL pour la mise à jour

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                updateUrl + "?id=" + etudiant.getId() + "&nom=" + etudiant.getNom() +
                        "&prenom=" + etudiant.getPrenom() + "&ville=" + etudiant.getVille() +
                        "&sexe=" + etudiant.getSexe(), // Passez les nouveaux paramètres
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Étudiant mis à jour", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged(); // Actualiser la liste des étudiants après la modification
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(stringRequest);
    }
    }




