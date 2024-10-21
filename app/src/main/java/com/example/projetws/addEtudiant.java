package com.example.projetws;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import android.provider.MediaStore;
import android.graphics.Bitmap;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class addEtudiant extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "addEtudiant";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_etudiant);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        add = findViewById(R.id.add);
        m = findViewById(R.id.m);
        f = findViewById(R.id.f);
        selectImage = findViewById(R.id.selectImage); // Initialiser le bouton
        imageView = findViewById(R.id.imageView); // Initialiser l'ImageView
        add.setOnClickListener(this);
        selectImage.setOnClickListener(this); // Ajouter l'écouteur pour le bouton

        // Configurer le Spinner avec les villes
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.villes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ville.setAdapter(adapter);

        // Utilisez le conteneur principal pour les WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Button clicked");
        if (v == selectImage) {
            // Ouvrir la galerie pour sélectionner une image
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        } else if (v == add) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
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
                    Toast.makeText(addEtudiant.this, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(addEtudiant.this, "Erreur lors de l'ajout de l'étudiant", Toast.LENGTH_SHORT).show();
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
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri); // Afficher l'image sélectionnée
            Log.d(TAG, "Image URI: " + imageUri); // Log de l'URI de l'image
        }
    }
}
