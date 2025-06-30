package me.theoria.wifimuscles.data.managers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.view.fragments.BarChartFragment;
import me.theoria.wifimuscles.view.fragments.ChartFragment;
import me.theoria.wifimuscles.view.fragments.StatsFragment;

public class NavigationManager {

    private final FragmentManager fragmentManager;
    private final Activity activity;

    private final Fragment chartFragment = new ChartFragment();
    private final Fragment barChartFragment = new BarChartFragment();
    private final Fragment statsFragment = new StatsFragment();

    public NavigationManager(@NonNull Activity activity, @NonNull FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    public void setupBottomNavigation(BottomNavigationView bottomNavigationView, int fragmentContainerId) {
        setFragment(chartFragment, fragmentContainerId); // Default fragment

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.mainChart) {
                setFragment(chartFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.barChart) {
                setFragment(barChartFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.donate) {
                openDonationLink();
                return true;
            } else if (itemId == R.id.stats) {
                setFragment(statsFragment, fragmentContainerId);
                return true;
            } else if (itemId == R.id.share) {
                openShareLink();
                return true;
            }
            return false;
        });
    }

    private void setFragment(Fragment fragment, int containerId) {
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }

    private void openDonationLink() {
        String paypalDonationUrl = "https://www.paypal.com/ncp/payment/T62DKS2TW4GRN";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paypalDonationUrl));
        activity.startActivity(browserIntent);
    }

    private void openShareLink() {
        String shareLinkUrl = "https://play.google.com/store/apps/details?id=me.theoria.wifimuscles";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareLinkUrl));
        activity.startActivity(browserIntent);
    }


}

