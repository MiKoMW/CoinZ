package io.github.mikomw.coinz.ui.activity;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.rateDBOperator;
import io.github.mikomw.coinz.util.Date;
import io.github.mikomw.coinz.util.DateInfo;
import io.github.mikomw.coinz.util.DownloadFileTask;
import io.github.mikomw.coinz.util.IO;

public class LoadingActivity extends AppCompatActivity {

    String tag = "LoadingActivity";
    DateInfo dateinfo = Date.getDateInfo();
    private rateDBOperator rateDBOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_loading);
        //downloadTodayMap();
        downExchangeRate();
    }

    private void downExchangeRate(){

        ArrayList<String> urls = Date.get30DaysUrl();
        SQLiteDatabase  db=SQLiteDatabase.openOrCreateDatabase("/data/user/0/io.github.mikomw.coinz/files/rate.db",null);


        rateDBOperator = new rateDBOperator(this);

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setDate("1997-08-26");
        exchangeRate.setPENY(1);
        exchangeRate.setQUID(2);
        exchangeRate.setDOLR(3);
        exchangeRate.setSHIL(4);


        rateDBOperator.delete("1997-08-26");

        if(rateDBOperator.CheckIsDataAlreadyInDBorNot("1997-08-26")){
            System.out.println("In!");
        }else{
            rateDBOperator.add(exchangeRate);
            System.out.println("NotIN!");
        }


        /*
        String tempURL = Date.getTodayUrl();
        DownloadFileTask myTask = new DownloadFileTask();
        ArrayList<String> result = new ArrayList<>();

        try {
            result.add(myTask.execute(tempURL).get());
            System.out.println(result);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
            e.printStackTrace();
        }*/



    }


    private void downloadTodayMap() {
        String todayURL = Date.getTodayUrl();
        DownloadFileTask myTask = new DownloadFileTask();
        String result = null;
        try {
            result = myTask.execute(todayURL).get();
            IO.writeToFile(this.getFilesDir(),"todayMap.geojson",result);
            System.out.println(result);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
            e.printStackTrace();
        }
    }





}



