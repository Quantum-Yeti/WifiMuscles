package me.theoria.wifimuscles.data.managers.nav;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.view.fragments.ChartFragment;
import me.theoria.wifimuscles.view.fragments.StatsFragment;
import me.theoria.wifimuscles.view.fragments.SupportFragment;

public class NavigationManager {

    // Reference to FragmentManager to perform transactions
    private final FragmentManager fragmentManager;

    // Reference to the host Activity for context-related operations
    private final Activity activity;

    // References to each fragment within the BottomNavigationView
    private final Fragment chartFragment = new ChartFragment();
    private final Fragment statsFragment = new StatsFragment();
    private final Fragment supportFragment = new SupportFragment();

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
            } else if (itemId == R.id.donate) {
                openDonationLink();
                return true;
            } else if (itemId == R.id.share) {
                openShareLink();
                return true;
            }
            return false;
        });
    }

    public void setupTopNavigationBar() {

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


}

