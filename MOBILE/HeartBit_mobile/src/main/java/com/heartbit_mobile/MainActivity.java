package com.heartbit_mobile;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.heartbit_mobile.databinding.ActivityMainBinding;
import com.heartbit_mobile.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.navigation_support:
                        replaceFragment(new SupportFragment());
                    break;
                case R.id.navigation_calendar:
                    break;
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navigation_dashboard:
                    break;
                case R.id.navigation_settings:
                    break;
            }

            return true;
        });
    }
    private void replaceFragment(Fragment fragment)
    {
    FragmentManager fragmentManager=getSupportFragmentManager();
    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.frame_layout,fragment);
    fragmentTransaction.commit();
    }
}