package me.theoria.wifimuscles.view.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.theoria.wifimuscles.databinding.FragmentDonateBinding;

public class DonateFragment extends Fragment {

    private FragmentDonateBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDonateBinding.inflate(inflater, container, false);

        binding.btnDonate.setOnClickListener(v -> {
            // Replace this URL with your PayPal donation link
            String paypalUrl = "https://www.paypal.com/donate?hosted_button_id=YOUR_BUTTON_ID";

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(paypalUrl));
            startActivity(intent);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}