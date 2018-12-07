package io.github.mikomw.coinz.util;

import java.util.ArrayList;

/**
 * A DataInfo class contains the download url for the past 30 days and the data in yyyy/MM/dd/ format
 * as a string.
 *
 * @author Songbo Hu
 */

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
