package me.theoria.wifimuscles.view.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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

        // Initialize top navigation from NavigationManager
        DrawerLayout drawerLayout = findViewById(R.id.top_drawer_layout);
        NavigationView navigationView = findViewById(R.id.top_nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize bottom navigation from NavigationManager
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_drawer_menu, menu);
        return true;  // Display the menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // just a toast for now
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


