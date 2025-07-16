package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.theoria.wifimuscles.R;

public class TipFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "description";
    private static final String ARG_DETAIL = "detail";
    private static final String ARG_ICON = "icon";

    public static TipFragment newInstance(String title, String desc, String detail, int iconResId) {
        TipFragment fragment = new TipFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, desc);
        args.putString(ARG_DETAIL, detail);
        args.putInt(ARG_ICON, iconResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_tip, container, false);

        TextView title = view.findViewById(R.id.tipTitle);
        TextView description = view.findViewById(R.id.tipDescription);
        TextView detail = view.findViewById(R.id.tipDetail);
        ImageView icon = view.findViewById(R.id.tipIcon);


        Bundle args = getArguments();
        if (args != null) {
            title.setText(args.getString(ARG_TITLE));
            description.setText(args.getString(ARG_DESC));
            detail.setText(args.getString(ARG_DETAIL));
        }

        if (args != null && args.containsKey(ARG_ICON)) {
            int iconResId = args.getInt(ARG_ICON);
            icon.setImageResource(iconResId);
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }

        return view;
    }


}
