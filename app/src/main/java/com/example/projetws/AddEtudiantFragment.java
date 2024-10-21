package com.example.projetws;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.app.ActivityCompat;

import android.provider.MediaStore;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import com.example.projetws.beans.Etudiant;

public class AddEtudiantFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddEtudiantFragment";

    private EditText nom;
    private EditText prenom;
    private Spinner ville;
    private RadioButton m;
    private RadioButton f;
    private Button add;
    private Button selectImage; // Bouton pour sélectionner l'image
    private ImageView imageView; // ImageView pour afficher l'image
    private Uri imageUri; // URI de l'image sélectionnée
    private RequestQueue requestQueue;
    private String insertUrl = "http://10.0.2.2/project/ws/createEtudiant.php"; // Vérifiez cette URL
    private EtudiantAdapter adapter ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liez le layout à la vue de votre fragment
        View view = inflater.inflate(R.layout.activity_add_etudiant, container, false);

        // Initialiser les vues
        nom = view.findViewById(R.id.nom);
        prenom = view.findViewById(R.id.prenom);
        ville = view.findViewById(R.id.ville);
        add = view.findViewById(R.id.add);
        m = view.findViewById(R.id.m);
        f = view.findViewById(R.id.f);
        selectImage = view.findViewById(R.id.selectImage);
        imageView = view.findViewById(R.id.imageView);

        // Configurer le Spinner avec les villes
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.villes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ville.setAdapter(adapter);

        // Ajoutez des écouteurs pour les boutons
        add.setOnClickListener(this);
        selectImage.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == selectImage) {
            // Ouvrir la galerie pour sélectionner une image
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        } else if (v == add) {
            // Ajouter l'étudiant
            requestQueue = Volley.newRequestQueue(getContext());
            StringRequest request = new StringRequest(Request.Method.POST,
                    insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Type type = new TypeToken<Collection<Etudiant>>() {}.getType();
                    Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                    for (Etudiant e : etudiants) {
                        Log.d(TAG, e.toString());
                    }
                    // Affichage d'un message Toast pour confirmer l'ajout
                    Toast.makeText(getContext(), "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String jsonError = new String(error.networkResponse.data);
                        Log.e(TAG, "Erreur: " + jsonError);
                    } else {
                        Log.e(TAG, "Erreur: " + error.getMessage());
                    }
                    Toast.makeText(getContext(), "Erreur lors de l'ajout de l'étudiant", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String sexe = m.isChecked() ? "homme" : "femme";
                    HashMap<String, String> params = new HashMap<>();
                    params.put("nom", nom.getText().toString());
                    params.put("prenom", prenom.getText().toString());
                    params.put("ville", ville.getSelectedItem().toString());
                    params.put("sexe", sexe);

                    // Ajouter l'image en Base64
                    if (imageUri != null) {
                        String imageBase64 = encodeImageToBase64(imageUri);
                        params.put("image", imageBase64);
                    }

                    return params;
                }
            };
            requestQueue.add(request);
        }
    }

    // Méthode pour convertir l'image URI en Base64
    private String encodeImageToBase64(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'encodage de l'image: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri); // Afficher l'image sélectionnée
            Log.d(TAG, "Image URI: " + imageUri); // Log de l'URI de l'image
        } else {
            Log.d(TAG, "Aucune image sélectionnée ou résultat incorrect.");
        }

    }

}

