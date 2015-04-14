package com.matih.auctionsystem.Managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Matih on 24/2/2015.
 * Initialized singleton shared instance of Database for app
 */
public class InitManager {

    private static InitManager sharedInstance;
    private Context context;
    private DatabaseManager dbManager;
    private SQLiteDatabase database;
    private long currentUserId;

    public static InitManager getSharedInstance(){
        if(sharedInstance == null){
            sharedInstance = new InitManager();
        }
        return sharedInstance;
    }

    public void initInstance(Context context){
        if(getContext() == null){
            setContext(context);
        }

        if(getDbManager() == null){
            setDbManager(new DatabaseManager(getContext()));
        }

        if(getDatabase() == null){
            setDatabase(getDbManager().getWritableDatabase());
        }
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    private void setDbManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    private void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }
}
