package com.example.kiwi.tpprogresstracker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kiwi.tpprogresstracker.fragment.CurrentWeekFragment;
import com.example.kiwi.tpprogresstracker.fragment.NextWeekFragment;
import com.example.kiwi.tpprogresstracker.fragment.TodayFragment;

import java.util.Vector;

/**
 * Created by kiwi on 9/21/2016.
 */
public class DashboardPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    String[] mTabTitle = new String[]{"Today", "Current Week", "Next Week"};
    Vector<Fragment> fragments ;

    public DashboardPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        fragments = new Vector<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TodayFragment();
                break;
            case 1:
                fragment = new CurrentWeekFragment();
                break;
            case 2:
                fragment = new NextWeekFragment();
                break;
        }
        fragments.add(fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTabTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }

    public Vector<Fragment> getAllFragments(){
        return fragments;
    }


}
