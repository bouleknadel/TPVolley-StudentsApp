package com.example.projetws;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ListeEtudiantFragment(); // Fragment pour la première page
            case 1:
                return new AddEtudiantFragment();   // Fragment pour la deuxième page
            default:
                return new ListeEtudiantFragment(); // Valeur par défaut si le nombre dépasse
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Nombre total de pages (onglets)
    }
}
