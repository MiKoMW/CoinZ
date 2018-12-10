package io.github.mikomw.coinz.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A helper class help us to query the date information and the map download url.
 *
 * @author Songbo Hu
 */

public class Date {

    /**
     * A help function to get today map download url.
     * @return today download Url
     */
    public static String getTodayUrl() {
        Calendar calendar = Calendar.getInstance();
        String  today = new SimpleDateFormat("yyyy/MM/dd/").format(calendar.getTime());
        return "http://homepages.inf.ed.ac.uk/stg/coinz/" +  today +"coinzmap.geojson";
    }

    /**
     * A help function to get map download urls for past 30 days.
     * @return download Urls for 30 days
     */
    public static ArrayList<String> get30DaysUrl(){

        ArrayList<String> ans = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for(int con = 0; con < 30; con++) {
            @SuppressLint("SimpleDateFormat") String temp_day = new SimpleDateFormat("yyyy/MM/dd/").format(calendar.getTime());
            String tempURL = "http://homepages.inf.ed.ac.uk/stg/coinz/" + temp_day + "coinzmap.geojson";
            ans.add(tempURL);
            calendar.add(Calendar.DATE, -1);
        }
        return ans;
    }

    /**
     * A help function to get date information.
     * @return DateInfo class which contains date information we need for this game.
     */
    public static DateInfo getDateInfo(){
        ArrayList<String> monthURL = get30DaysUrl();
        String todayURL = getTodayUrl();

        ArrayList<String> month = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String today  = new SimpleDateFormat("yyyy/MM/dd/").format(calendar.getTime());
        for(int con = 0; con < 30; con++) {
            @SuppressLint("SimpleDateFormat") String temp_day = new SimpleDateFormat("yyyy/MM/dd/").format(calendar.getTime());
            month.add(temp_day);
            calendar.add(Calendar.DATE, -1);
        }

        return new DateInfo(todayURL,today,monthURL,month);

    }

}
