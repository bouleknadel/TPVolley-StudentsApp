package com.example.projetws;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.projetws.beans.Etudiant;

public class ListeEtudiant extends AppCompatActivity {

    private static final String TAG = "ListeEtudiant";
    private ListView listView;
    private List<Etudiant> etudiants;
    private List<Etudiant> filteredEtudiants;
    private RequestQueue requestQueue;
    private String url = "http://10.0.2.2/project/ws/loadEtudiant.php";
    private EtudiantAdapter adapter;
    private EditText searchBar;
    private Spinner filterSpinner;
    private ImageView searchIcon;
    private String currentFilterCriteria = "Nom"; // Critère par défaut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_etudiant);

        listView = findViewById(R.id.listViewEtudiants);
        searchBar = findViewById(R.id.searchBar);
        filterSpinner = findViewById(R.id.filterSpinner);
        searchIcon = findViewById(R.id.searchIcon);

        etudiants = new ArrayList<>();
        filteredEtudiants = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        // Configurer le spinner pour choisir le critère de filtrage
        setupFilterSpinner();
        fetchEtudiants();

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

        // Gestion de la saisie dans la barre de recherche
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterEtudiants(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Initialiser le Spinner avec les options de filtrage
    private void setupFilterSpinner() {
        String[] filterOptions = {"Nom", "Prénom", "Ville", "Sexe"}; // Critères disponibles
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterOptions);
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

    // Récupérer la liste des étudiants depuis l'API
    private void fetchEtudiants() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        Type type = new TypeToken<List<Etudiant>>() {}.getType();
                        etudiants = new Gson().fromJson(response.toString(), type);
                        filteredEtudiants.addAll(etudiants);
                        adapter = new EtudiantAdapter(ListeEtudiant.this, filteredEtudiants);
                        listView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Erreur: " + error.getMessage());
                        Toast.makeText(ListeEtudiant.this, "Erreur lors de la récupération des étudiants", Toast.LENGTH_SHORT).show();
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
}
