package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.theoria.wifimuscles.R;

/**
 * A fragment class displaying a meter and helpful placement tips for pods/extenders.
 */
public class PodPlacerFragment extends Fragment {



    public PodPlacerFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method creating an instance of the PodPlacer fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PodPlacerFragment fragment(PodPlacerFragment fragment) {

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pod_placer, container, false);
    }
}