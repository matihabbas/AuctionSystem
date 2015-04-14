package com.matih.auctionsystem.Utils;

import java.util.Date;

/**
 * Created by Matih on 26/2/2015.
 */
public class DateUtils {

    /*
    * Gets the string indicating time left for bid
    * */
    public static String getTimeLeftString(Date laterDate){
        Date dateNow = new Date();
        long diff = laterDate.getTime() - dateNow.getTime();

        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if(diffMinutes <= 0) return "Expired";
        if(diffMinutes < 60) return (diffMinutes + " minutes left");
        if(diffHours < 24) return (diffHours + " hours left");
        return (diffDays + " days left");
    }

    /*
    * Returns whether bid time has expired
    * */
    public static boolean isExpired(Date laterDate){
        Date dateNow = new Date();
        long diff = laterDate.getTime() - dateNow.getTime();

        if(diff <= 0) return true;
        else return false;
    }
}
