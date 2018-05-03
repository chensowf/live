package cn.mz.live.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.mz.live.base.BaseFragment;


public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragments;

    private String[] titles;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments,
        String[] titles) {
        super(fm);
        mFragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
