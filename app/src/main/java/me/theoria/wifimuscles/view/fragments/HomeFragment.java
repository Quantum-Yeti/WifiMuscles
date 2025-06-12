package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.theoria.wifimuscles.R;

/**
 * {@code HomeFragment} is a {@link Fragment} that inflates the {@code fragment_home.xml} layout.
 * {@link #onCreate(Bundle)} initializes the fragment.
 * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} inflates the fragment layout.
 */
public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}