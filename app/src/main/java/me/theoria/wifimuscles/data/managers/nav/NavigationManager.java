package me.theoria.wifimuscles.data.managers.nav;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.view.fragments.AboutFragment;
import me.theoria.wifimuscles.view.fragments.BarChartFragment;
import me.theoria.wifimuscles.view.fragments.ChartFragment;
import me.theoria.wifimuscles.view.fragments.StatsFragment;
import me.theoria.wifimuscles.view.fragments.SupportFragment;
import me.theoria.wifimuscles.view.fragments.WifiListFragment;

public class NavigationManager {

    // Reference to FragmentManager to perform transactions
    private final FragmentManager fragmentManager;

    // Reference to the host Activity for context-related operations
    private final Activity activity;

    // References to each fragment within the BottomNavigationView
    private final Fragment chartFragment = new ChartFragment();
    private final Fragment barChartFragment = new BarChartFragment();
    private final Fragment statsFragment = new StatsFragment();
    private final Fragment supportFragment = new SupportFragment();
    private final Fragment wifiListFragment = new WifiListFragment();

    // References to each item within the TopNavigationDrawer
    private final Fragment aboutFragment = new AboutFragment();

    //private final Fragment barChartFragment = new BarChartFragment(); // Optional, currently not used.

    public NavigationManager(@NonNull Activity activity, @NonNull FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    /**
     * Method to set up the BottomNavigationView with listeners that switch between fragments.
     * @param bottomNavigationView Configures the BottomNavigationView.
     * @param fragmentContainerId Initializes the default fragment and handles navigation.
     */
    public void setupBottomNavigation(BottomNavigationView bottomNavigationView, int fragmentContainerId) {
        setFragment(chartFragment, fragmentContainerId); // Default fragment

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.mainChart) {
                setFragment(chartFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.stats) {
                setFragment(statsFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.support) {
                setFragment(supportFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.wifiList) {
                setFragment(wifiListFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.share) {
                openShareLink();
                return true;
            }
            return false;
        });
    }

    /**
     * Method for the top navigation drawer and associated logic switching between fragment onClick.
     */
    public void setupTopNavigationDrawer(NavigationView navigationView, int fragmentContainerId) {
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_lineChart) {
                setFragment(chartFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.nav_barChart) {
                setFragment(barChartFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.nav_troubleShoot) {
                setFragment(supportFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.nav_advStats) {
                setFragment(statsFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.nav_wifiList) {
                setFragment(wifiListFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.nav_about) {
                setFragment(aboutFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.nav_share) {
                openShareLink();
                return true;
            } else if (itemId == R.id.nav_donate) {
                openDonationLink();
                return true;
            } else if (itemId == R.id.nav_feedback) {
                sendFeedBackEmail();
            } else {
                return false;
            }

            // Close the drawer upon item click
            DrawerLayout drawerLayout = (DrawerLayout) navigationView.getParent();
            drawerLayout.closeDrawers();
            return true;
        });
    }

    /**
     * Method to replace the fragment of a specified container with the provided fragment.
     * @param fragment The new fragment.
     * @param containerId The ID of the container to place the fragment.
     */
    private void setFragment(Fragment fragment, int containerId) {
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }

    // Navigation Helpers --//
    /**
     * Method to open Url to PayPal donations.
     */
    private void openDonationLink() {
        String paypalDonationUrl = "https://www.paypal.com/ncp/payment/T62DKS2TW4GRN";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paypalDonationUrl));
        activity.startActivity(browserIntent);
    }

    /**
     * Method to open Url to Google Play.
     */
    private void openShareLink() {
        String shareLinkUrl = "https://play.google.com/store/apps/details?id=me.theoria.wifimuscles";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareLinkUrl));
        activity.startActivity(browserIntent);
    }

    /**
     * Method to open email in an app.
     */
    public void sendFeedBackEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:theoriasoft@proton.me"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "WiFi Muscles Feedback");
        try {
            activity.startActivity(Intent.createChooser(intent, "Send Feedback"));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(activity, "No email app found", Toast.LENGTH_SHORT).show();
        }

    }


}

