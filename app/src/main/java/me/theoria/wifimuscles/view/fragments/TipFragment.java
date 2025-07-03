package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.theoria.wifimuscles.R;

public class TipFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "description";
    private static final String ARG_DETAIL = "detail";

    public static TipFragment newInstance(String title, String desc, String detail) {
        TipFragment fragment = new TipFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, desc);
        args.putString(ARG_DETAIL, detail);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tip, container, false);

        TextView title = view.findViewById(R.id.tipTitle);
        TextView description = view.findViewById(R.id.tipDescription);
        TextView detail = view.findViewById(R.id.tipDetail);


        Bundle args = getArguments();
        if (args != null) {
            title.setText(args.getString(ARG_TITLE));
            description.setText(args.getString(ARG_DESC));
            detail.setText(args.getString(ARG_DETAIL));
        }


        return view;
    }


}
