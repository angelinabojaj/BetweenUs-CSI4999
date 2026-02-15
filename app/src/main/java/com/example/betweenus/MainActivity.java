package com.example.betweenus;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.betweenus.fragments.MemoriesFragment;
import com.example.betweenus.fragments.ProfileFragment;
import com.example.betweenus.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.betweenus.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.navView.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {

                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();

                } else if (id == R.id.navigation_memories) {
                    selectedFragment = new MemoriesFragment();

                } else if (id == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_main, selectedFragment)
                            .commit();
                    return true;
                }

                return false;
            };

}
