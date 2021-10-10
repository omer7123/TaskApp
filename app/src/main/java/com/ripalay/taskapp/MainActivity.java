package com.ripalay.taskapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ripalay.taskapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String MY_SETTINGS = "my_settings";
    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.profileFragment)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

  /*      SharedPreferences sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);

        if (!hasVisited) {
            navController.navigate(R.id.boardFragment);
            sp.edit()
                    .putBoolean("hasVisited", true)
                    .apply(); // не забудьте подтвердить изменения
        } else {
            navController.navigate(R.id.navigation_home);
        }
*/

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_home ||
                        destination.getId() == R.id.navigation_dashboard ||
                        destination.getId() == R.id.navigation_notifications ||
                        destination.getId() == R.id.profileFragment) {
                    navView.setVisibility(View.VISIBLE);

                } else {
                    navView.setVisibility(View.GONE);
                }

                if (destination.getId() == R.id.boardFragment || destination.getId() == R.id.taskFragment) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
            }
        });

        if (true)
            navController.navigate(R.id.boardFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }


}