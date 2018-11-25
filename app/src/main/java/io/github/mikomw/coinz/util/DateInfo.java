package io.github.mikomw.coinz.util;

import java.util.ArrayList;

public class DateInfo {

    String todayURL;
    ArrayList<String> monthURL;
    String today;
    ArrayList<String> month;


    public DateInfo(String todayURL,String today, ArrayList<String> monthURL, ArrayList<String> month){
        this.today = today;
        this.todayURL = todayURL;
        this.month = month;
        this.monthURL = monthURL;
    }



}
