package com.matih.auctionsystem.Classes;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Matih on 24/2/2015.
 */
public class TabElement {

    private int tabID;
    private String name;
    private Fragment fragment;
    private Context context;

    public TabElement(int tabID, String name, Fragment fragment, Context context) {
        this.tabID = tabID;
        this.name = name;
        this.fragment = fragment;
        this.context = context;
    }

    public int getTabID() {
        return tabID;
    }

    public void setTabID(int tabID) {
        this.tabID = tabID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
