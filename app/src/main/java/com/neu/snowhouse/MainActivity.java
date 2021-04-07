package com.neu.snowhouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAuthorizationStatus();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    public void initAuthorizationStatus() {
        String userName = SessionManagement.getUserName(this);
        if (userName.equals("")) {
            Intent intent = new Intent(this, UserAuthorizeActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            assert navHostFragment != null;
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }
    }
}