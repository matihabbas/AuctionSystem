package com.matih.auctionsystem.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.matih.auctionsystem.Adapters.MainViewPagerAdapter;
import com.matih.auctionsystem.Classes.TabElement;
import com.matih.auctionsystem.Fragments.CurrentAuctionsFragment;
import com.matih.auctionsystem.Managers.InitManager;
import com.matih.auctionsystem.R;
import com.matih.auctionsystem.Utils.SlidingTabLayout;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity{

    private ViewPager viewPager_Main;
    private MainViewPagerAdapter tab_adapter;
    private ArrayList<TabElement> tabElements;
    private Toolbar toolbar;
    private SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tabs = (SlidingTabLayout)findViewById(R.id.tabs);

        toolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        if(tabElements == null){
            tabElements = new ArrayList<>();

            CurrentAuctionsFragment allFrag = new CurrentAuctionsFragment();
            Bundle allBundle = new Bundle();
            allBundle.putInt(CurrentAuctionsFragment.BUNDLE_NAME_FRAG_TYPE, CurrentAuctionsFragment.PAGE_TYPE_ALL);
            allFrag.setArguments(allBundle);

            CurrentAuctionsFragment myBidsFrag = new CurrentAuctionsFragment();
            Bundle myBidsBundle = new Bundle();
            myBidsBundle.putInt(CurrentAuctionsFragment.BUNDLE_NAME_FRAG_TYPE, CurrentAuctionsFragment.PAGE_TYPE_MY_BIDS);
            myBidsFrag.setArguments(myBidsBundle);

            CurrentAuctionsFragment myPostsFrag = new CurrentAuctionsFragment();
            Bundle myPostsBundle = new Bundle();
            myPostsBundle.putInt(CurrentAuctionsFragment.BUNDLE_NAME_FRAG_TYPE, CurrentAuctionsFragment.PAGE_TYPE_MY_POSTS);
            myPostsFrag.setArguments(myPostsBundle);

            tabElements.add(new TabElement(0, getResources().getString(R.string.title_tab_all_auctions), allFrag, this));
            tabElements.add(new TabElement(1, getResources().getString(R.string.title_tab_my_bid), myBidsFrag, this));
            tabElements.add(new TabElement(2, getResources().getString(R.string.title_tab_my_posts), myPostsFrag, this));
        }

        if(tab_adapter == null){
            tab_adapter = new MainViewPagerAdapter(getSupportFragmentManager());
            tab_adapter.setTabElements(tabElements);
        }

        viewPager_Main = (ViewPager)findViewById(R.id.viewPager_Main);
        viewPager_Main.setAdapter(tab_adapter);

        tabs.setSelectedIndicatorColors(Color.parseColor("#2d7bef"), Color.parseColor("#DF0003"), Color.parseColor("#0ECF00"));
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(viewPager_Main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
        }
        else if(id == R.id.action_new){
            Intent intent = new Intent(this, NewItemActivity.class);
            intent.putExtra(NewItemActivity.EXTRA_NAME_USER_ID, InitManager.getSharedInstance().getCurrentUserId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Logout the user
    * */
    private void logout(){
        SharedPreferences loginPreferences = getSharedPreferences(LoginActivity.LOGIN_PREF_NAME, Context.MODE_PRIVATE);
        loginPreferences.edit().putInt(LoginActivity.LOGIN_PREF_KEY_USER_ID, -1).commit();
        InitManager.getSharedInstance().setCurrentUserId(-1);

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
