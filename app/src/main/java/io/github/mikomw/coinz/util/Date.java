package io.github.mikomw.coinz.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Date {

    public static String getTodayUrl() {
        Calendar calendar = Calendar.getInstance();
        String  today = new SimpleDateFormat("yyyy/MM/dd/").format(calendar.getTime());
        String todayURL ="http://homepages.inf.ed.ac.uk/stg/coinz/" +  today +"coinzmap.geojson";
        return todayURL;
    }

    public static ArrayList<String> get30DaysUrl(){

        ArrayList<String> ans = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for(int con = 0; con < 30; con++) {
            String temp_day = new SimpleDateFormat("yyyy/MM/dd/").format(calendar.getTime());
            String tempURL = "http://homepages.inf.ed.ac.uk/stg/coinz/" + temp_day + "coinzmap.geojson";
            ans.add(tempURL);
            calendar.add(Calendar.DATE, -1);
        }
        return ans;
    }

    public static DateInfo getDateInfo(){
        ArrayList<String> monthURL = get30DaysUrl();
        String todayURL = getTodayUrl();

        ArrayList<String> month = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String today  = new SimpleDateFormat("yyyy/MM/dd/").format(calendar.getTime());
        for(int con = 0; con < 30; con++) {
            String temp_day = new SimpleDateFormat("yyyy/MM/dd/").format(calendar.getTime());
            month.add(temp_day);
            calendar.add(Calendar.DATE, -1);
        }


        DateInfo dateInfo = new DateInfo(todayURL,today,monthURL,month);
        return dateInfo;

    }





}
