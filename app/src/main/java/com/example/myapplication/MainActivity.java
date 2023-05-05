package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // We will supply our own TextView.
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                final int id = navDestination.getId();
                final String label = navDestination.getLabel().toString();

                binding.toolbarTextView.setText(label);

                if (id == R.id.navigation_dashboard) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    // Custom up icon.
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_keyboard_arrow_left_24);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        });
        // https://stackoverflow.com/questions/76104744/how-to-remove-space-curve-cradle-among-fab-and-bottomappbar
        final BottomAppBar bottomAppBar = binding.bottomAppBar;
        bottomAppBar.setFabCradleMargin(-bottomAppBar.getFabCradleMargin());

        final ViewTreeObserver vto = bottomAppBar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    bottomAppBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    bottomAppBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)(binding.toolbarContentAndBackgroundImageFrameLayout.getLayoutParams());
                marginLayoutParams.bottomMargin = bottomAppBar.getHeight();
                binding.toolbarContentAndBackgroundImageFrameLayout.setLayoutParams(marginLayoutParams);
            }
        });
    }

}