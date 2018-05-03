package cn.mz.live.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Pair;

import java.util.List;

public class MainAdapter extends FragmentPagerAdapter {

    private List<Pair<String, Fragment>> items;

    public MainAdapter(FragmentManager fm, List<Pair<String, Fragment>> items) {
        super(fm);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position).second;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).first;
    }

}
