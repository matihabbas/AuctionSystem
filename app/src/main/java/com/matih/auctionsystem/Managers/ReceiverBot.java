package com.matih.auctionsystem.Managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.matih.auctionsystem.Activities.LoginActivity;
import com.matih.auctionsystem.Classes.AuctionItem;
import com.matih.auctionsystem.Classes.Bid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Matih on 25/2/2015.
 * Runs random bidding
 */
public class ReceiverBot extends BroadcastReceiver{
    private static final String DEBUG_TAG = ReceiverBot.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(DEBUG_TAG, "onReceive");
        //Toast.makeText(context, "Bidding in background", Toast.LENGTH_SHORT).show();
        long userIdLoggedIn = - 1;

        // Initialize Database
        InitManager.getSharedInstance().initInstance(context.getApplicationContext());

        // Get current logged in user used to bid
        SharedPreferences loginPreferences = context.getApplicationContext().getSharedPreferences(LoginActivity.LOGIN_PREF_NAME, Context.MODE_PRIVATE);
        if(loginPreferences != null){
            try{
                userIdLoggedIn = loginPreferences.getLong(LoginActivity.LOGIN_PREF_KEY_USER_ID, -1);
            }
            catch (Exception e){
                Log.e(DEBUG_TAG, "No user logged in");
            }
        }

        if(userIdLoggedIn == -1){
            // No user is logged in
            return;
        }

        List<AuctionItem> auctionItems = new ArrayList<AuctionItem>();
        // Get all bids available to user
        auctionItems = InitManager.getSharedInstance().getDbManager().getAllAuctions(InitManager.getSharedInstance().getDatabase(), userIdLoggedIn);

        // Generate random pick
        Random random = new Random();
        int randomBidNumber = random.nextInt(auctionItems.size());

        AuctionItem pickedRandomItem = auctionItems.get(randomBidNumber);

        Bid randBid;
        // Does bid already exist?
        randBid = InitManager.getSharedInstance().getDbManager().getBidByUserIdItemId(InitManager.getSharedInstance().getDatabase(),
                                        userIdLoggedIn,
                                        pickedRandomItem.getObjectId());

        if(randBid == null){
            // New bid
            randBid = new Bid();
            randBid.setAuctionItemId(pickedRandomItem.getObjectId());
            randBid.setUserId(userIdLoggedIn);
            randBid.setBidTime(new Date());
            randBid.setBidAmount(pickedRandomItem.getPrice() + (random.nextDouble() * 10.0));
            InitManager.getSharedInstance().getDbManager().addBid(InitManager.getSharedInstance().getDatabase(),
                    randBid);
            Toast.makeText(context, "Bidding randomly on an new auction", Toast.LENGTH_SHORT).show();
        }
        else{
            // Update bid
            randBid.setBidAmount(randBid.getBidAmount() + (random.nextDouble() * 10.0));
            InitManager.getSharedInstance().getDbManager().updateBid(InitManager.getSharedInstance().getDatabase(),
                    randBid.getBidId(), randBid.getBidAmount());
            Toast.makeText(context, "Bid updated on item", Toast.LENGTH_SHORT).show();
        }
    }
}
