package me.theoria.wifimuscles.view.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.view.fragments.BarChartFragment;
import me.theoria.wifimuscles.view.fragments.ChartFragment;
import me.theoria.wifimuscles.view.fragments.DonateFragment;
import me.theoria.wifimuscles.view.fragments.HomeFragment;
import me.theoria.wifimuscles.view.fragments.StatsFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * Navigation methods for setting the current fragment, movement between fragments and
         * initializing them on the MainActivity.
         * TODO: Move into NavigationComponent
         */

        // Fragment declaration
        //Fragment homeFragment = new HomeFragment();
        Fragment mainFragment = new ChartFragment();
        Fragment barChartFragment = new BarChartFragment();
        Fragment donateFragment = new DonateFragment();
        Fragment statsFragment = new StatsFragment();

        // Set current fragment from setCurrentFragment
        setCurrentFragment(mainFragment);
        // Initialize bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Bottom navigation logic
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.mainChart) {
                setCurrentFragment(mainFragment);
            } else if (itemId == R.id.barChart) {
                setCurrentFragment(barChartFragment);
            } else if (itemId == R.id.donate) {
                findViewById(R.id.donate).setOnClickListener(v -> {
                    String paypalDonationUrl = "https://www.paypal.com/ncp/payment/T62DKS2TW4GRN";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paypalDonationUrl));
                    startActivity(browserIntent);
                });
            } else if (itemId == R.id.stats) {
                setCurrentFragment(statsFragment);
            }
            return true;
        });

        /**
         * Implement and initialize Google AdMob
         */
        // Initialize Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {});

        // Reference AdView and load ad
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     * Method: setCurrentFragment
     * Description: Replaces the fragment within the fragment container block from
     * the main activity.
     * @param fragment
     */
    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}


