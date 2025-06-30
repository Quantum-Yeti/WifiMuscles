package me.theoria.wifimuscles.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.view.adapters.ChartPagerAdapter;

public class ChartPagerFragment extends Fragment {

    private ViewPager2 viewPager;
    private ChartPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_pager, container, false);
        viewPager = view.findViewById(R.id.chartViewPager);

        pagerAdapter = new ChartPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        return view;
    }
}
