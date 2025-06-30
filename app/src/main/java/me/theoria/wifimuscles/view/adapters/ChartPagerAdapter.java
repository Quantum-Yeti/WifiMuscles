package me.theoria.wifimuscles.view.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import me.theoria.wifimuscles.view.fragments.BarChartFragment;
import me.theoria.wifimuscles.view.fragments.ChartFragment;

public class ChartPagerAdapter extends FragmentStateAdapter {

    public ChartPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new ChartFragment() : new BarChartFragment();

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
