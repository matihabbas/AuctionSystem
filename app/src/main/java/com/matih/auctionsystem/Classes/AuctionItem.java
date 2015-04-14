package com.matih.auctionsystem.Classes;

import java.util.Date;

/**
 * Created by Matih on 24/2/2015.
 */
public class AuctionItem {

    public static final String TABLE = "auction_items";

    public static final String ID = "object_id";
    public static final String USER_ID = "user_id";
    public static final String WON_BY_USER_ID = "won_by_user_id";
    public static final String EXPIRY_DATE = "expiry_date";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_DESCRIPTION = "item_description";
    public static final String PRICE = "price";
    public static final String IMAGE_FILENAME = "image_filename";
    public static final String IS_SOLD = "is_sold";

    private long objectId;
    private long userId;
    private long wonByUserId;
    private Date expiry;
    private String itemName;
    private String itemDescription;
    private String itemImageFilename;
    private double price;
    private boolean isSold;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getWonByUserId() {
        return wonByUserId;
    }

    public void setWonByUserId(long wonByUserId) {
        this.wonByUserId = wonByUserId;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemImageFilename() {
        return itemImageFilename;
    }

    public void setItemImageFilename(String itemImageFilename) {
        this.itemImageFilename = itemImageFilename;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean isSold) {
        this.isSold = isSold;
    }
}
