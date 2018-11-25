package io.github.mikomw.coinz.util;

import java.util.ArrayList;

public class DateInfo {

    public String todayURL;
    public ArrayList<String> monthURL;
    public String today;
    public ArrayList<String> month;


    public DateInfo(String todayURL,String today, ArrayList<String> monthURL, ArrayList<String> month){
        this.today = today;
        this.todayURL = todayURL;
        this.month = month;
        this.monthURL = monthURL;
    }



}
