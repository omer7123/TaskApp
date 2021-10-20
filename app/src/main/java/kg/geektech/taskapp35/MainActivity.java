package kg.geektech.taskapp35;

import android.os.Bundle;
import android.view.View;

import com.geektech.taskapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.geektech.taskapp.databinding.ActivityMainBinding;

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

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.profileFragment)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

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

                if (destination.getId() == R.id.boardFragment || destination.getId() == R.id.taskFragment ||
                        destination.getId() == R.id.profileFragment || destination.getId() == R.id.loginFragment) {

                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
            }
        });
        Prefs prefs = new Prefs(this);

        if (!prefs.isBoardShow()) {
            navController.navigate(R.id.boardFragment);
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null && prefs.isBoardShow()) {
            navController.navigate(R.id.loginFragment);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }


}