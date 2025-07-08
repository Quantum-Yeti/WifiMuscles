package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.managers.SpeedTestManager;

public class SpeedTestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_speed_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView resultTextView = view.findViewById(R.id.speedTestResult);
        ProgressBar progressBar = view.findViewById(R.id.speedTestProgressBar);

        SpeedTestManager manager = new SpeedTestManager(
                requireActivity(),
                resultTextView,
                progressBar
        );

        manager.runSpeedTest();
    }
}
