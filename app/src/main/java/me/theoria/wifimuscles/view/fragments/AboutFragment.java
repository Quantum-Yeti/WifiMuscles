package me.theoria.wifimuscles.view.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import me.theoria.wifimuscles.R;

public class AboutFragment extends Fragment {

    private TextView versionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Pull resources from layout
        TextView appNameTextView = view.findViewById(R.id.aboutAppName);
        versionTextView = view.findViewById(R.id.aboutVersion);
        TextView developerTextView = view.findViewById(R.id.aboutDeveloper);

        // Automates version from package manager
        setAppVersion();

        // returns the view
        return view;
    }

    /**
     * Method to pull version from package manager.
     */
    private void setAppVersion() {
        try {
            PackageManager pm = requireContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(requireContext().getPackageName(), 0);
            versionTextView.setText(String.format("Version %s", pi.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            versionTextView.setText(R.string.version_unavailable);
        }
    }

    // String array for libraries
    List<String> libraries = Arrays.asList(
      "MPChart - https://github.com/PhilJay/MPAndroidChart",
      "Lottie - https://github.com/airbnb/lottie-android"
    );



}