package com.matih.auctionsystem.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.matih.auctionsystem.Classes.TabElement;

import java.util.ArrayList;

/**
 * Created by Matih on 24/2/2015.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<TabElement> tabElements;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return tabElements.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return tabElements.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabElements.get(position).getName();
    }

    public void setTabElements(ArrayList<TabElement> tabElements) {
        this.tabElements = tabElements;
    }
}
