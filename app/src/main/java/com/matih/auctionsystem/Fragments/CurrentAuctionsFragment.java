package com.matih.auctionsystem.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matih.auctionsystem.Activities.AuctionItemDetailActivity;
import com.matih.auctionsystem.Adapters.ItemsListViewAdapter;
import com.matih.auctionsystem.Classes.AuctionItem;
import com.matih.auctionsystem.Classes.Bid;
import com.matih.auctionsystem.Managers.InitManager;
import com.matih.auctionsystem.R;

import java.util.ArrayList;

/**
 * Created by Matih on 24/2/2015.
 */
public class CurrentAuctionsFragment extends Fragment {

    private static final String DEBUG_TAG = CurrentAuctionsFragment.class.getSimpleName();

    public static final int PAGE_TYPE_ALL = 0;
    public static final int PAGE_TYPE_MY_BIDS = 1;
    public static final int PAGE_TYPE_MY_POSTS = 2;
    public static final String BUNDLE_NAME_FRAG_TYPE = "fragType";

    private int fragmentType;
    private View fragmentView;
    private ListView listViewItems;
    private long currentUserId;
    ArrayList<AuctionItem> auctionItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFragmentType(getArguments().getInt(BUNDLE_NAME_FRAG_TYPE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auctions_current, container, false);
        fragmentView = view;
        listViewItems = (ListView)view.findViewById(R.id.listView_auction_items);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCurrentUserId(InitManager.getSharedInstance().getCurrentUserId());
        new updateFragmentFromDb().execute();
    }

    /*
    * Open Auction Detail
    * */
    private void openItem(long itemId){
        Intent detailIntent = new Intent(getActivity(), AuctionItemDetailActivity.class);
        detailIntent.putExtra(AuctionItemDetailActivity.EXTRA_NAME_ITEM_ID, itemId);
        detailIntent.putExtra(AuctionItemDetailActivity.EXTRA_NAME_CURRENT_USER_ID, getCurrentUserId());
        startActivity(detailIntent);
    }

    /*
    * Update fragment in Background thread
    * */
    private class updateFragmentFromDb extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            auctionItems = new ArrayList<>();

            if(getFragmentType() == PAGE_TYPE_ALL){
                auctionItems = InitManager.getSharedInstance().getDbManager().getAllAuctions(InitManager.getSharedInstance().getDatabase(), getCurrentUserId());
            }
            else if(getFragmentType() == PAGE_TYPE_MY_BIDS){
                ArrayList<Bid> bidList = InitManager.getSharedInstance().getDbManager().getBidsByUserId(
                        InitManager.getSharedInstance().getDatabase(),
                        getCurrentUserId());
                for(Bid bid:bidList){
                    auctionItems.add(InitManager.getSharedInstance().getDbManager().getAuctionItemById(
                            InitManager.getSharedInstance().getDatabase(),
                            bid.getAuctionItemId()));
                }
            }
            else if(getFragmentType() == PAGE_TYPE_MY_POSTS){
                auctionItems = InitManager.getSharedInstance().getDbManager().getAuctionItemsByUserId(InitManager.getSharedInstance().getDatabase(), getCurrentUserId());
            }

            Log.d(DEBUG_TAG, "Found " + auctionItems.size() + " items");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ItemsListViewAdapter itemsListViewAdapter = new ItemsListViewAdapter();
            itemsListViewAdapter.setAuctionItems(auctionItems);
            listViewItems.setAdapter(itemsListViewAdapter);

            listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long itemId = auctionItems.get(position).getObjectId();
                    openItem(itemId);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new updateFragmentFromDb().execute();
    }

    /*
     * Update fragment on swipe
    * */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            if (!isVisibleToUser) {
                new updateFragmentFromDb().execute();
            }
        }
    }

    private void setFragmentType(int fragmentType) {
        this.fragmentType = fragmentType;
    }

    public int getFragmentType() {
        return fragmentType;
    }

    private long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }
}
