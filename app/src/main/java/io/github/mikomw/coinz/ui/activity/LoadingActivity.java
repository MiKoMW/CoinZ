package io.github.mikomw.coinz.ui.activity;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.rateDBOperator;
import io.github.mikomw.coinz.util.Date;
import io.github.mikomw.coinz.util.DateInfo;
import io.github.mikomw.coinz.util.DownloadFileTask;
import io.github.mikomw.coinz.util.IO;
import io.github.mikomw.coinz.util.UpdateRateTask;

public class LoadingActivity extends AppCompatActivity {

    String tag = "LoadingActivity";
    DateInfo dateinfo;
    private rateDBOperator rateDBOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dateinfo = Date.getDateInfo();
    }

    public void onStart() {
        super.onStart();
        setContentView(R.layout.activity_loading);
        downloadTodayMap();
        downExchangeRate();
    }

    private void downExchangeRate(){

        ArrayList<String> dates = dateinfo.month;
        SQLiteDatabase  db=SQLiteDatabase.openOrCreateDatabase("/data/user/0/io.github.mikomw.coinz/files/rate.db",null);
        rateDBOperator = new rateDBOperator(this);

        UpdateRateTask myTask = new UpdateRateTask();
        ArrayList<ExchangeRate>  result = null;



        ArrayList<String> updatedDates = new ArrayList<>();
        List<String> alreadyDone = rateDBOperator.queryAlllRate();
        for(String date : dates){
            if(!alreadyDone.contains(date)){
                System.out.println("Update" + date);
                updatedDates.add(date);
            }
        }

        try {
            result = myTask.execute(updatedDates).get();

        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
            e.printStackTrace();
        }

        if(result == null){
            System.out.println("UpdateTaskWRONG!");
        }else {
            for(ExchangeRate rate : result){
                if(!rateDBOperator.CheckIsDataAlreadyInDBorNot(rate.getDate()))
                rateDBOperator.add(rate);
            }
        }



        List<ExchangeRate> temp =  rateDBOperator.queryMany();

        /*
        for(ExchangeRate ex : temp){
            System.out.println(ex.getDate());
            System.out.println(ex.getDOLR());
            System.out.println(ex.getPENY());
            System.out.println(ex.getQUID());
            System.out.println(ex.getSHIL());
        }
*/
    }


    private void downloadTodayMap() {
        String todayURL = dateinfo.todayURL;
        DownloadFileTask myTask = new DownloadFileTask();
        String result = null;
        try {
            result = myTask.execute(todayURL).get();
            IO.writeToFile(this.getFilesDir(),"todayMap.geojson",result);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
            e.printStackTrace();
        }
    }





}



