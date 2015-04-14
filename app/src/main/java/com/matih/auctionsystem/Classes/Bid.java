package com.matih.auctionsystem.Classes;

import java.util.Date;

/**
 * Created by Matih on 24/2/2015.
 */
public class Bid {

    public static final String TABLE = "bid";

    public static final String ID = "bid_id";
    public static final String OBJECT_ID = "object_id";
    public static final String USER_ID = "user_id";
    public static final String BID_TIME = "bid_time";
    public static final String BID_AMOUNT = "bid_amount";

    private long bidId;
    private long auctionItemId;
    private long userId;
    private Date bidTime;
    private double bidAmount;

    public long getBidId() {
        return bidId;
    }

    public void setBidId(long bidId) {
        this.bidId = bidId;
    }

    public long getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(long auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }
}
