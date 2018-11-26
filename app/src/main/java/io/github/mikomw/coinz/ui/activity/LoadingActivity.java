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
        //downloadTodayMap();
        //downExchangeRate();
    }





}



