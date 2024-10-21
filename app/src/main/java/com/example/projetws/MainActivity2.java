package com.example.projetws;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView; // Assurez-vous que cette ligne est présente
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.core.app.ShareCompat;

public class MainActivity2 extends AppCompatActivity  {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageView shareIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // Assure-toi que le layout correspond

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        shareIcon = findViewById(R.id.share_icon); // Récupérer l'icône de partage

        // Définir l'adaptateur pour ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Crée une vue personnalisée pour chaque onglet
            View customView = getLayoutInflater().inflate(R.layout.custom_tab, null);

            ImageView icon = customView.findViewById(R.id.icon);
            TextView text = customView.findViewById(R.id.text);

            switch (position) {
                case 0:
                    text.setText("Liste Étudiants");
                    icon.setImageResource(R.drawable.ic_list);  // Définir l'icône pour Liste Étudiants
                    // Retirer la ligne suivante pour garder la couleur d'origine
                    // icon.setColorFilter(getResources().getColor(R.color.blue));
                    break;
                case 1:
                    text.setText("Ajouter Étudiant");
                    icon.setImageResource(R.drawable.ic_add);  // Définir l'icône pour Ajouter Étudiant
                    // Retirer la ligne suivante pour garder la couleur d'origine
                    // icon.setColorFilter(getResources().getColor(R.color.blue));
                    break;
            }

            // Appliquer la vue personnalisée à l'onglet
            tab.setCustomView(customView);
        }).attach();

        // Ajouter un listener pour l'icône de partage
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity2", "Icône de partage cliquée"); // Log pour le débogage
                shareContent(); // Appeler la méthode de partage
            }
        });
    }

    // Méthode pour partager le contenu
    private void shareContent() {
        String txt = "Découvrez notre application de gestion des étudiants !"; // Message à partager
        String mimeType = "text/plain"; // Type de contenu

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Partager via")
                .setText(txt)
                .startChooser();
    }
}
