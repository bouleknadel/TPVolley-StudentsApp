package com.example.projetws;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projetws.addEtudiant;  // Ajoutez ceci si AddEtudiant est dans le même package
import com.example.projetws.ListeEtudiant;  // Ajoutez ceci si AddEtudiant est dans le même package


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddEtudiant = findViewById(R.id.btnAddEtudiant);
        Button btnListeEtudiant = findViewById(R.id.btnListeEtudiant);

        btnAddEtudiant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addEtudiant.class);
                startActivity(intent);
            }
        });

        btnListeEtudiant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListeEtudiant.class);
                startActivity(intent);
            }
        });
    }
}
