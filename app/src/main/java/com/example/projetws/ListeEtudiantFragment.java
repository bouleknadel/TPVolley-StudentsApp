package com.example.projetws;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.FileNotFoundException;
import android.app.Activity;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.app.Dialog; // Import for Dialog
import android.content.Intent; // Import for Intent
import android.net.Uri; // Import for Uri
import android.os.Bundle; // Import for Bundle
import android.text.Editable; // Import for Editable
import android.text.TextWatcher; // Import for TextWatcher
import android.util.Log; // Import for Log
import android.view.LayoutInflater; // Import for LayoutInflater
import android.view.View; // Import for View
import android.view.ViewGroup; // Import for ViewGroup
import android.widget.AdapterView; // Import for AdapterView
import android.widget.ArrayAdapter; // Import for ArrayAdapter
import android.widget.EditText; // Import for EditText
import android.widget.ImageView; // Import for ImageView
import android.widget.ListView; // Import for ListView
import android.widget.Spinner; // Import for Spinner
import android.widget.Toast; // Import for Toast
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull; // Import for NonNull
import androidx.annotation.Nullable; // Import for Nullable
import androidx.fragment.app.Fragment; // Import for Fragment

import com.android.volley.Request; // Import for Request
import com.android.volley.RequestQueue; // Import for RequestQueue
import com.android.volley.Response; // Import for Response
import com.android.volley.VolleyError; // Import for VolleyError
import com.android.volley.toolbox.JsonArrayRequest; // Import for JsonArrayRequest
import com.android.volley.toolbox.Volley; // Import for Volley
import com.example.projetws.beans.Etudiant; // Import for Etudiant class
import com.google.gson.Gson; // Import for Gson
import com.google.gson.reflect.TypeToken; // Import for TypeToken

import org.json.JSONArray; // Import for JSONArray

import java.io.InputStream; // Ajout de l'import pour InputStream
import java.lang.reflect.Type; // Import for Type
import java.util.ArrayList; // Import for ArrayList
import java.util.List; // Import for List


public class ListeEtudiantFragment extends Fragment {
    private static final String TAG = "ListeEtudiantFragment";
    private ListView listView;
    private EtudiantAdapter adapter;
    private List<Etudiant> etudiants;
    private List<Etudiant> filteredEtudiants;
    private RequestQueue requestQueue;
    private String url = "http://10.0.2.2/project/ws/loadEtudiant.php";
    private String currentFilterCriteria = "Nom"; // Critère de filtrage par défaut
    private Spinner filterSpinner; // Spinner pour sélectionner le critère
    private EditText searchBar; // Barre de recherche
    private ImageView searchIcon; // Icône de recherche
    private Bitmap bitmap ;
    private Uri SelectedImage ;
    private ImageView imageView ;
    private ImageView dialogImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_liste_etudiant, container, false);
        listView = view.findViewById(R.id.listViewEtudiants);
        filterSpinner = view.findViewById(R.id.filterSpinner); // Assurez-vous que ce ID correspond à votre layout
        searchBar = view.findViewById(R.id.searchBar); // Assurez-vous que ce ID correspond à votre layout
        searchIcon = view.findViewById(R.id.searchIcon); // Assurez-vous que ce ID correspond à votre layout
        imageView = view.findViewById(R.id.imageView);
        etudiants = new ArrayList<>();
        filteredEtudiants = new ArrayList<>(); // Initialiser la liste filtrée
        adapter = new EtudiantAdapter(getContext(), filteredEtudiants); // Adapter avec la liste filtrée
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Trouver le bouton de modification dans la vue de l'élément
                Button buttonModifier = view.findViewById(R.id.buttonModifier);

                // Vérifier si le clic était sur le bouton de modification
                if (buttonModifier != null && buttonModifier.isPressed()) {
                    Etudiant etudiant = (Etudiant) buttonModifier.getTag();
                    showEditDialog(etudiant);
                }
       }
});

        requestQueue = Volley.newRequestQueue(requireContext());
        fetchEtudiants();
        setupFilterSpinner(); // Initialiser le spinner de filtrage



        // Ajouter un écouteur de texte pour filtrer en temps réel
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterEtudiants(charSequence.toString()); // Filtrer en fonction du texte saisi
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Gestion du clic sur l'icône de recherche
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBar.getVisibility() == View.GONE) {
                    searchBar.setVisibility(View.VISIBLE);
                    searchBar.requestFocus();
                } else {
                    searchBar.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    public void fetchEtudiants() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Réponse reçue : " + response.toString());
                        Type type = new TypeToken<List<Etudiant>>() {}.getType();
                        etudiants = new Gson().fromJson(response.toString(), type);
                        Log.d(TAG, "Nombre d'étudiants récupérés : " + etudiants.size());
                        filteredEtudiants.clear();
                        filteredEtudiants.addAll(etudiants); // Afficher tous les étudiants par défaut
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Adapter notifié du changement de données");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Erreur: " + error.getMessage(), error);
                        Toast.makeText(getContext(), "Erreur lors de la récupération des étudiants", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    // Filtrer les étudiants en fonction de la saisie et du critère sélectionné
    private void filterEtudiants(String query) {
        filteredEtudiants.clear();
        if (query.isEmpty()) {
            filteredEtudiants.addAll(etudiants); // Si la recherche est vide, afficher tous les étudiants
        } else {
            for (Etudiant etudiant : etudiants) {
                boolean matches = false;
                switch (currentFilterCriteria) {
                    case "Nom":
                        matches = etudiant.getNom().toLowerCase().contains(query.toLowerCase());
                        break;
                    case "Prénom":
                        matches = etudiant.getPrenom().toLowerCase().contains(query.toLowerCase());
                        break;
                    case "Ville":
                        matches = etudiant.getVille().toLowerCase().contains(query.toLowerCase());
                        break;
                    case "Sexe":
                        matches = etudiant.getSexe().toLowerCase().contains(query.toLowerCase());
                        break;
                }
                if (matches) {
                    filteredEtudiants.add(etudiant);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Initialiser le Spinner avec les options de filtrage
    private void setupFilterSpinner() {
        String[] filterOptions = {"Nom", "Prénom", "Ville", "Sexe"}; // Critères disponibles
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, filterOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFilterCriteria = filterOptions[position]; // Mettre à jour le critère sélectionné
                filterEtudiants(searchBar.getText().toString()); // Appliquer le filtrage immédiat
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            SelectedImage = data.getData();
            // Mettre à jour l'ImageView dans le dialogue de modification
            imageView.setImageURI(SelectedImage);
}
    }


    private void showEditDialog(Etudiant etudiant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.dialog_edit_etudiant, null);

        // Initialiser tous les champs du dialogue
        EditText editTextNom = view.findViewById(R.id.editTextNom);
        EditText editTextPrenom = view.findViewById(R.id.editTextPrenom);
        Spinner spinnerVille = view.findViewById(R.id.spinnerVille);
        RadioGroup radioGroupSexe = view.findViewById(R.id.radioGroupSexe);
        dialogImageView = view.findViewById(R.id.imageView);
        Button buttonSelectImage = view.findViewById(R.id.selectImage);

        // Pré-remplir les champs avec les valeurs actuelles de l'étudiant
        editTextNom.setText(etudiant.getNom());
        editTextPrenom.setText(etudiant.getPrenom());

        // Configurer le Spinner avec les villes
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.villes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVille.setAdapter(adapter);

        // Sélectionner la ville actuelle
        int spinnerPosition = adapter.getPosition(etudiant.getVille());
        spinnerVille.setSelection(spinnerPosition);

        // Sélectionner le sexe actuel
        if ("homme".equalsIgnoreCase(etudiant.getSexe())) {
            radioGroupSexe.check(R.id.m);
        } else {
            radioGroupSexe.check(R.id.f);
        }

        // Charger l'image actuelle de l'étudiant
        String baseUrl = "http://10.0.2.2/project/";
        String imageUrl = baseUrl + etudiant.getImage();
        Glide.with(requireContext())
                .load(imageUrl)
                .error(R.drawable.image_error)
                .into(dialogImageView);

        // Configurer le bouton de sélection d'image
        buttonSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        builder.setView(view);
        builder.setTitle("Modifier l'étudiant");

        builder.setPositiveButton("Modifier", null); // On définit le listener plus tard

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        // Définir le listener du bouton positif
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view1 -> {
                // Récupérer les nouvelles valeurs
                String nouveauNom = editTextNom.getText().toString().trim();
                String nouveauPrenom = editTextPrenom.getText().toString().trim();
                String nouvelleVille = spinnerVille.getSelectedItem().toString();
                String nouveauSexe = radioGroupSexe.getCheckedRadioButtonId() == R.id.m ? "homme" : "femme";

                // Vérification des champs
                if (nouveauNom.isEmpty() || nouveauPrenom.isEmpty()) {
                    Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Mettre à jour l'objet étudiant
                etudiant.setNom(nouveauNom);
                etudiant.setPrenom(nouveauPrenom);
                etudiant.setVille(nouvelleVille);
                etudiant.setSexe(nouveauSexe);

                // Appeler la méthode pour envoyer la requête de mise à jour
                //updateEtudiant(etudiant);

                dialog.dismiss();
            });
        });

        dialog.show();
    }




}

