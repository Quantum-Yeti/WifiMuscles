package me.theoria.wifimuscles.view.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.managers.ads.AdManager;
import me.theoria.wifimuscles.data.managers.nav.NavigationManager;
import me.theoria.wifimuscles.utils.PermissionUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Location + Wifi Permission Check from Utils Class
        PermissionUtils.checkWifiAndLocationEnabled(this);

        // Fragment declaration
        //Fragment mainFragment = new ChartFragment();

        // Set current fragment from setCurrentFragment helper
        setCurrentFragment(new Fragment());

        // Top Menu navigation
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // Initialize navigation from NavigationManager
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationManager navigationManager = new NavigationManager(this, getSupportFragmentManager());
        navigationManager.setupBottomNavigation(bottomNavigationView, R.id.fragment_container);

        // Initialize Google AdView
        AdView mAdView = findViewById(R.id.adView);
        AdManager.initAds(this, mAdView);

        // Check Google Play for updates
        //checkForUpdates();

    }

    /**
     * Method: setCurrentFragment
     * Description: Replaces the fragment within the fragment container block from
     * the main activity.
     *
     * @param fragment sets the selected fragment to the fragment container
     */
    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}


