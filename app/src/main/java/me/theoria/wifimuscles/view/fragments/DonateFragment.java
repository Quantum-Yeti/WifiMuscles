package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import me.theoria.wifimuscles.R;

/**
 *{@code DonateFragment} is a {@link Fragment} that displays the donation UI.
 * {@link #onCreate(Bundle)} is called to initialize the fragment creation.
 * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} is called to inflate the fragment's view.
 */
public class DonateFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donate, container, false);
    }
}