package me.theoria.wifimuscles.data.managers;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import me.theoria.wifimuscles.R;

public class AdManager {

    public static void initAds(Activity activity, AdView adView) {

        // Initialize Mobile Ads SDK
        MobileAds.initialize(activity, initializationStatus -> {});
        // Reference AdView and load ad
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

}
