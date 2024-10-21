package com.example.projetws;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Appliquer les insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialiser l'animation
        ImageView logoImageView = findViewById(R.id.imageView);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        logoImageView.startAnimation(rotateAnimation); // Démarrer l'animation

        // Démarrer le thread pour passer à l'activité suivante
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000); // Délai de 2 secondes
                    Intent intent = new Intent(SplashActivity.this, MainActivity2.class); // Changez MainActivity avec l'activité principale de votre application
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
    }
}
