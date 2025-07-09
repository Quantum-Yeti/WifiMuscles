package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.managers.info.SupportTipManager;
import me.theoria.wifimuscles.data.model.SupportModel;
import me.theoria.wifimuscles.view.adapters.SupportAdapter;

public class SupportFragment extends Fragment {

    private RecyclerView supportRecyclerView;
    private SupportAdapter supportAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        initializeRecyclerView(view);
        return view;
    }

    private void initializeRecyclerView(View rootView) {
        supportRecyclerView = rootView.findViewById(R.id.supportRecyclerView);
        supportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        supportAdapter = new SupportAdapter(
                SupportTipManager.getSupportTips(requireContext()),
                this::onTipSelected
        );
        supportRecyclerView.setAdapter(supportAdapter);
    }


    private void onTipSelected(SupportModel tip) {
        TipFragment tipFragment = TipFragment.newInstance(
                tip.getTitle(),
                tip.getDescription(),
                tip.getDetailedDescription()
        );

        // Replace the current fragment with TipFragment and add transaction to back stack
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, tipFragment) // change fragment_container to your container id
                .addToBackStack(null)
                .commit();
    }
}
