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

import java.util.Arrays;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.SupportModel;
import me.theoria.wifimuscles.view.adapters.SupportAdapter;

public class SupportFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        recyclerView = view.findViewById(R.id.supportRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<SupportModel> tips = Arrays.asList(
                new SupportModel("Check Wi-Fi Signal Strength", "Move closer to the router or remove obstructions.", "Testing longer detailed description on click."),
                new SupportModel("Restart Your Router", "Unplug for 10 seconds and plug it back in.", "Testing longer detailed description on click."),
                new SupportModel("Forget & Reconnect to Network", "Go to Wi-Fi settings and reconnect.", "Testing longer detailed description on click."),
                new SupportModel("Switch Frequency Band", "Try switching between 2.4GHz and 5GHz.", "Testing longer detailed description on click."),
                new SupportModel("Reduce Interference", "Turn off unused Wi-Fi devices or move away from microwave ovens.", "Testing longer detailed description on click.")
        );

        recyclerView.setAdapter(new SupportAdapter(tips));

        return view;
    }
}
