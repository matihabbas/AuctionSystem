package com.matih.auctionsystem.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matih.auctionsystem.Classes.AuctionItem;
import com.matih.auctionsystem.Classes.Bid;
import com.matih.auctionsystem.Classes.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Matih on 24/2/2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = DatabaseManager.class.getSimpleName();

    private static final String DATABASE_NAME = "auctionsystem.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;
    private SimpleDateFormat parserDate = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.US);

    public DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AuctionItem.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Bid.TABLE);

        this.onCreate(db);
    }

    public void executeSchemaFile(SQLiteDatabase db, Context context, String filename){
        BufferedReader bufferedReader = null;
        try{
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                db.execSQL(line);
            }
        }
        catch (FileNotFoundException fe){
            Log.e(DEBUG_TAG, "Schema file not found: " + fe.getMessage());
        }
        catch (IOException e){
            Log.e(DEBUG_TAG, "Error reading schema file: " + e.getMessage());
        }
        finally {
            if(bufferedReader != null){
                try{
                    bufferedReader.close();
                }
                catch (IOException e){
                    Log.e(DEBUG_TAG, "Error closing schema file: " + e.getMessage());
                }
            }
        }
    }

    public ArrayList<AuctionItem> getAllAuctions(SQLiteDatabase db, long currentUserId){
        ArrayList<AuctionItem> autionItemList = new ArrayList<AuctionItem>();
        Cursor c = db.query(false,
                            AuctionItem.TABLE,
                            new String[]{ AuctionItem.ID,
                                          AuctionItem.USER_ID,
                                          AuctionItem.WON_BY_USER_ID,
                                          AuctionItem.EXPIRY_DATE,
                                          AuctionItem.ITEM_NAME,
                                          AuctionItem.ITEM_DESCRIPTION,
                                          AuctionItem.PRICE,
                                          AuctionItem.IMAGE_FILENAME,
                                          AuctionItem.IS_SOLD},
                            AuctionItem.USER_ID + " != ?" + " AND " + AuctionItem.IS_SOLD + " == ?" ,
                            new String[] { String.valueOf(currentUserId), "0"},
                            null, null, null, null);

        if(c.moveToFirst()){
            do{
                AuctionItem auctionItem = new AuctionItem();
                auctionItem.setObjectId(c.getInt(c.getColumnIndex(AuctionItem.ID)));
                auctionItem.setUserId(c.getInt(c.getColumnIndex(AuctionItem.USER_ID)));
                auctionItem.setWonByUserId(c.getInt(c.getColumnIndex(AuctionItem.WON_BY_USER_ID)));

                Date expiryDate = Calendar.getInstance().getTime();
                try{
                    expiryDate =  parserDate.parse(c.getString(c.getColumnIndex(AuctionItem.EXPIRY_DATE)));
                }
                catch (ParseException pe){
                    Log.e(DEBUG_TAG, "Error parsing item date: " + pe.getMessage());
                    continue;
                }
                auctionItem.setExpiry(expiryDate);

                auctionItem.setItemName(c.getString(c.getColumnIndex(AuctionItem.ITEM_NAME)));
                auctionItem.setItemDescription(c.getString(c.getColumnIndex(AuctionItem.ITEM_DESCRIPTION)));
                auctionItem.setPrice(Double.parseDouble(c.getString(c.getColumnIndex(AuctionItem.PRICE))));
                auctionItem.setItemImageFilename(c.getString(c.getColumnIndex(AuctionItem.IMAGE_FILENAME)));
                auctionItem.setSold(c.getInt(c.getColumnIndex(AuctionItem.IS_SOLD)) != 0);

                autionItemList.add(auctionItem);
            }
            while (c.moveToNext());
        }

        return autionItemList;
    }

    public ArrayList<AuctionItem> getAuctionItemsByUserId(SQLiteDatabase db, long userId){
        ArrayList<AuctionItem> autionItemList = new ArrayList<AuctionItem>();
        Cursor c = db.query(false,
                AuctionItem.TABLE,
                new String[]{ AuctionItem.ID,
                        AuctionItem.USER_ID,
                        AuctionItem.WON_BY_USER_ID,
                        AuctionItem.EXPIRY_DATE,
                        AuctionItem.ITEM_NAME,
                        AuctionItem.ITEM_DESCRIPTION,
                        AuctionItem.PRICE,
                        AuctionItem.IMAGE_FILENAME,
                        AuctionItem.IS_SOLD},
                " " + AuctionItem.USER_ID + " = ?",
                new String[] { String.valueOf(userId) },
                null, null, null, null);

        if(c.moveToFirst()){
            do{
                AuctionItem auctionItem = new AuctionItem();
                auctionItem.setObjectId(c.getInt(c.getColumnIndex(AuctionItem.ID)));
                auctionItem.setUserId(c.getInt(c.getColumnIndex(AuctionItem.USER_ID)));
                auctionItem.setWonByUserId(c.getInt(c.getColumnIndex(AuctionItem.WON_BY_USER_ID)));

                Date expiryDate = Calendar.getInstance().getTime();
                try{
                    expiryDate =  parserDate.parse(c.getString(c.getColumnIndex(AuctionItem.EXPIRY_DATE)));
                }
                catch (ParseException pe){
                    Log.e(DEBUG_TAG, "Error parsing item date: " + pe.getMessage());
                    continue;
                }
                auctionItem.setExpiry(expiryDate);

                auctionItem.setItemName(c.getString(c.getColumnIndex(AuctionItem.ITEM_NAME)));
                auctionItem.setItemDescription(c.getString(c.getColumnIndex(AuctionItem.ITEM_DESCRIPTION)));
                auctionItem.setPrice(Double.parseDouble(c.getString(c.getColumnIndex(AuctionItem.PRICE))));
                auctionItem.setItemImageFilename(c.getString(c.getColumnIndex(AuctionItem.IMAGE_FILENAME)));
                auctionItem.setSold(c.getInt(c.getColumnIndex(AuctionItem.IS_SOLD)) != 0);

                autionItemList.add(auctionItem);
            }
            while (c.moveToNext());
        }

        return autionItemList;
    }

    public AuctionItem getAuctionItemById(SQLiteDatabase db, long itemId){
        AuctionItem auctionItem = new AuctionItem();

        Cursor c = db.query(false,
                AuctionItem.TABLE,
                new String[]{ AuctionItem.ID,
                        AuctionItem.USER_ID,
                        AuctionItem.WON_BY_USER_ID,
                        AuctionItem.EXPIRY_DATE,
                        AuctionItem.ITEM_NAME,
                        AuctionItem.ITEM_DESCRIPTION,
                        AuctionItem.PRICE,
                        AuctionItem.IMAGE_FILENAME,
                        AuctionItem.IS_SOLD},
                " " + AuctionItem.ID + " = ?",
                new String[] { String.valueOf(itemId) },
                null, null, null, null);

        if(c != null){
            c.moveToFirst();

            auctionItem.setObjectId(c.getInt(c.getColumnIndex(AuctionItem.ID)));
            auctionItem.setUserId(c.getInt(c.getColumnIndex(AuctionItem.USER_ID)));
            auctionItem.setWonByUserId(c.getInt(c.getColumnIndex(AuctionItem.WON_BY_USER_ID)));



            Date expiryDate = Calendar.getInstance().getTime();
            try{
                expiryDate =  parserDate.parse(c.getString(c.getColumnIndex(AuctionItem.EXPIRY_DATE)));
            }
            catch (ParseException pe){
                Log.e(DEBUG_TAG, "Error parsing item date: " + pe.getMessage());
            }
            auctionItem.setExpiry(expiryDate);

            auctionItem.setItemName(c.getString(c.getColumnIndex(AuctionItem.ITEM_NAME)));
            auctionItem.setItemDescription(c.getString(c.getColumnIndex(AuctionItem.ITEM_DESCRIPTION)));
            auctionItem.setPrice(Double.parseDouble(c.getString(c.getColumnIndex(AuctionItem.PRICE))));
            auctionItem.setItemImageFilename(c.getString(c.getColumnIndex(AuctionItem.IMAGE_FILENAME)));
            auctionItem.setSold(c.getInt(c.getColumnIndex(AuctionItem.IS_SOLD)) != 0);
        }
        else{
            Log.e(DEBUG_TAG, "Nothing found!");
        }

        return auctionItem;
    }

    public long addAuctionItem(SQLiteDatabase db, AuctionItem item){
        ContentValues values = new ContentValues();
        values.put(AuctionItem.USER_ID, item.getUserId());
        values.put(AuctionItem.WON_BY_USER_ID, item.getWonByUserId());
        values.put(AuctionItem.EXPIRY_DATE, parserDate.format(item.getExpiry()));
        values.put(AuctionItem.ITEM_NAME, item.getItemName());
        values.put(AuctionItem.ITEM_DESCRIPTION, item.getItemDescription());
        values.put(AuctionItem.PRICE, Double.toString(item.getPrice()));
        values.put(AuctionItem.IMAGE_FILENAME, item.getItemImageFilename());
        values.put(AuctionItem.IS_SOLD, item.isSold() ? 1 : 0);

        long result = -1;
        try{
            result = db.insertOrThrow(AuctionItem.TABLE, null, values);
        }
        catch (SQLiteConstraintException ce){
            Log.e(DEBUG_TAG, "Error adding to user table: " + ce.getMessage());
            result = -2;
        }

        return result;
    }

    public void updateAuctionItem(SQLiteDatabase db, AuctionItem item){
        ContentValues values = new ContentValues();
        values.put(AuctionItem.USER_ID, item.getUserId());
        values.put(AuctionItem.WON_BY_USER_ID, item.getWonByUserId());
        values.put(AuctionItem.EXPIRY_DATE, parserDate.format(item.getExpiry()));
        values.put(AuctionItem.ITEM_NAME, item.getItemName());
        values.put(AuctionItem.ITEM_DESCRIPTION, item.getItemDescription());
        values.put(AuctionItem.PRICE, Double.toString(item.getPrice()));
        values.put(AuctionItem.IMAGE_FILENAME, item.getItemImageFilename());
        values.put(AuctionItem.IS_SOLD, item.isSold() ? 1 : 0);

        long result = -1;
        try{
            result = db.update(AuctionItem.TABLE, values, AuctionItem.ID + "=" + item.getObjectId(), null);
        }
        catch (SQLiteConstraintException ce){
            Log.e(DEBUG_TAG, "Error updating: " + ce.getMessage());
            result = -2;
        }
    }

    public void setAuctionItemSold(SQLiteDatabase db, long userId, boolean sold){
        ContentValues values = new ContentValues();
        values.put(AuctionItem.IS_SOLD, sold ? 1 : 0);

        long result = -1;
        try{
            result = db.update(AuctionItem.TABLE, values, AuctionItem.ID + "=" + userId, null);
        }
        catch (SQLiteConstraintException ce){
            Log.e(DEBUG_TAG, "Error updating: " + ce.getMessage());
            result = -2;
        }
    }

    public User getUser(SQLiteDatabase db, long userId){
        Cursor c = db.query(false,
                User.TABLE,
                new String[]{ User.ID,
                        User.NAME,
                        User.USERNAME,
                        User.PASSWORD},
                " " + User.ID + " = ?",
                new String[] { String.valueOf(userId) },
                null, null, null, null);

        if(c != null){
            c.moveToFirst();
        }

        User user = new User();
        user.setUserId(c.getLong(c.getColumnIndex(User.ID)));
        user.setName(c.getString(c.getColumnIndex(User.NAME)));
        user.setUsername(c.getString(c.getColumnIndex(User.USERNAME)));
        user.setPassword(c.getString(c.getColumnIndex(User.PASSWORD)));

        return user;
    }

    public String getUsername(SQLiteDatabase db, int userId){
        Cursor c = db.query(false,
                User.TABLE,
                new String[]{ User.ID,
                        User.NAME},
                " " + User.ID + " = ?",
                new String[] { String.valueOf(userId) },
                null, null, null, null);

        if(c != null){
            c.moveToFirst();
        }

        String username = c.getString(c.getColumnIndex(User.NAME));
        return username;
    }

    public long addUser(SQLiteDatabase db, User user){
        ContentValues values = new ContentValues();
        values.put(User.NAME, user.getName());
        values.put(User.USERNAME, user.getUsername());
        values.put(User.PASSWORD, user.getPassword());

        long result = -1;
        try{
            result = db.insertOrThrow(User.TABLE, null, values);
        }
        catch (SQLiteConstraintException ce){
            Log.e(DEBUG_TAG, "Error adding to user table: " + ce.getMessage());
            result = -2;
        }

        return result;
    }

    public long addBid(SQLiteDatabase db, Bid bid){
        ContentValues values = new ContentValues();
        values.put(Bid.USER_ID, bid.getUserId());
        values.put(Bid.OBJECT_ID, bid.getAuctionItemId());
        values.put(Bid.BID_AMOUNT, String.valueOf(bid.getBidAmount()));
        values.put(Bid.BID_TIME, parserDate.format(bid.getBidTime()));

        long result = -1;
        try{
            result = db.insertOrThrow(Bid.TABLE, null, values);
        }
        catch (SQLiteConstraintException ce){
            Log.e(DEBUG_TAG, "Error adding to user table: " + ce.getMessage());
            result = -2;
        }

        return result;
    }

    public void updateBid(SQLiteDatabase db, long bidId, double newBidAmount){
        ContentValues values = new ContentValues();
        values.put(Bid.BID_AMOUNT, String.valueOf(newBidAmount));

        long result = -1;
        try{
            result = db.update(Bid.TABLE, values, Bid.ID + "=" + bidId, null);
        }
        catch (SQLiteConstraintException ce){
            Log.e(DEBUG_TAG, "Error updating: " + ce.getMessage());
            result = -2;
        }
    }

    public ArrayList<Bid> getBidsByItemId(SQLiteDatabase db, long objectId){
        ArrayList<Bid> bidList = new ArrayList<Bid>();
        Cursor c = db.query(false,
                Bid.TABLE,
                new String[]{ Bid.ID,
                        Bid.OBJECT_ID,
                        Bid.USER_ID,
                        Bid.BID_TIME,
                        Bid.BID_AMOUNT},
                " " + Bid.OBJECT_ID + " = ?",
                new String[] { String.valueOf(objectId) },
                null, null, null, null);

        SimpleDateFormat parserDate = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.US);

        if(c.moveToFirst()){
            do{
                Bid bid = new Bid();
                bid.setUserId(c.getInt(c.getColumnIndex(Bid.ID)));
                bid.setAuctionItemId(c.getInt(c.getColumnIndex(Bid.OBJECT_ID)));

                Date bidDate = Calendar.getInstance().getTime();
                try{
                    bidDate =  parserDate.parse(c.getString(c.getColumnIndex(Bid.BID_TIME)));
                }
                catch (ParseException pe){
                    Log.e(DEBUG_TAG, "Error parsing bid date: " + pe.getMessage());
                    continue;
                }
                bid.setBidTime(bidDate);
                bid.setBidAmount(Double.parseDouble(c.getString(c.getColumnIndex(Bid.BID_AMOUNT))));
                bidList.add(bid);
            }
            while (c.moveToNext());
        }

        return bidList;
    }

    public ArrayList<Bid> getBidsByUserId(SQLiteDatabase db, long userId){
        ArrayList<Bid> bidList = new ArrayList<Bid>();
        Cursor c = db.query(false,
                Bid.TABLE,
                new String[]{ Bid.ID,
                        Bid.OBJECT_ID,
                        Bid.USER_ID,
                        Bid.BID_TIME,
                        Bid.BID_AMOUNT},
                " " + Bid.USER_ID + " = ?",
                new String[] { String.valueOf(userId) },
                null, null, null, null);

        if(c.moveToFirst()){
            do{
                Bid bid = new Bid();
                bid.setBidId(c.getLong(c.getColumnIndex(Bid.ID)));
                bid.setUserId(c.getLong(c.getColumnIndex(Bid.USER_ID)));
                bid.setAuctionItemId(c.getLong(c.getColumnIndex(Bid.OBJECT_ID)));

                Date bidDate = Calendar.getInstance().getTime();
                try{
                    bidDate =  parserDate.parse(c.getString(c.getColumnIndex(Bid.BID_TIME)));
                }
                catch (ParseException pe){
                    Log.e(DEBUG_TAG, "Error parsing bid date: " + pe.getMessage());
                    continue;
                }
                bid.setBidTime(bidDate);
                bid.setBidAmount(Double.parseDouble(c.getString(c.getColumnIndex(Bid.BID_AMOUNT))));
                bidList.add(bid);
            }
            while (c.moveToNext());
        }

        return bidList;
    }

    public Bid getBidByUserIdItemId(SQLiteDatabase db, long userId, long itemId){
        Bid bid = null;
        Cursor c = db.query(false,
                Bid.TABLE,
                new String[]{ Bid.ID,
                        Bid.OBJECT_ID,
                        Bid.USER_ID,
                        Bid.BID_TIME,
                        Bid.BID_AMOUNT},
                " " + Bid.USER_ID + " = ?" + " AND " + Bid.OBJECT_ID + " = ?",
                new String[] { String.valueOf(userId), String.valueOf(itemId) },
                null, null, null, null);

        if(c != null){
            if(c.moveToFirst()){
                bid = new Bid();
                bid.setBidId(c.getLong(c.getColumnIndex(Bid.ID)));
                bid.setUserId(c.getLong(c.getColumnIndex(Bid.USER_ID)));
                bid.setAuctionItemId(c.getLong(c.getColumnIndex(Bid.OBJECT_ID)));

                Date bidDate = Calendar.getInstance().getTime();
                try{
                    bidDate =  parserDate.parse(c.getString(c.getColumnIndex(Bid.BID_TIME)));
                }
                catch (ParseException pe){
                    Log.e(DEBUG_TAG, "Error parsing bid date: " + pe.getMessage());
                }
                bid.setBidTime(bidDate);
                bid.setBidAmount(Double.parseDouble(c.getString(c.getColumnIndex(Bid.BID_AMOUNT))));
            }
        }

        return bid;
    }

    public long authenticateUser(SQLiteDatabase db, String username, String password){
        Cursor c = db.query(false,
                User.TABLE,
                new String[]{ User.ID,
                        User.USERNAME,
                        User.PASSWORD},
                " " + User.USERNAME + " = ?",
                new String[] { String.valueOf(username) },
                null, null, null, null);

        long userId = -1;

        if(c != null){
            c.moveToFirst();
            String passwordInDb = c.getString(c.getColumnIndex(User.PASSWORD));
            if(passwordInDb.equals(password)) {
                userId = c.getLong(c.getColumnIndex(User.ID));
            }
        }
        return userId;
    }
}
