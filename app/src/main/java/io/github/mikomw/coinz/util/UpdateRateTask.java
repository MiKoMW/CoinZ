package io.github.mikomw.coinz.util;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.rateDBOperator;


public class UpdateRateTask extends AsyncTask<ArrayList<String>, Void, ArrayList<ExchangeRate>>{
    private static final String tag = "UpdateRateTask";
    private io.github.mikomw.coinz.db.rateDBOperator rateDBOperator;

    @Override
    protected ArrayList<ExchangeRate> doInBackground(ArrayList<String>... dates) {

        ArrayList<ExchangeRate> ans = new ArrayList<>();
        try {
            for(String date : dates[0]){
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setDate(date);
                String tempURL ="http://homepages.inf.ed.ac.uk/stg/coinz/" +  date +"coinzmap.geojson";
                exchangeRate =  readStream(downloadUrl(new URL(tempURL)),exchangeRate);
                ans.add(exchangeRate);
            }
            } catch (IOException e) {
            e.printStackTrace();
        }

        return ans;

    }



    private InputStream downloadUrl(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000); // milliseconds!
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    @NonNull
    private ExchangeRate readStream(InputStream stream, ExchangeRate exchangeRate ) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        String temp = result.toString("UTF-8");

        String SHIL = temp.substring(temp.indexOf("\"SHIL\": ") + 7);
        String DOLR = temp.substring(temp.indexOf("\"DOLR\": ") + 7);
        String QUID = temp.substring(temp.indexOf("\"QUID\": ") + 7);
        String PENY = temp.substring(temp.indexOf("\"PENY\": ") + 7);
        SHIL = SHIL.substring(0,SHIL.indexOf(","));
        DOLR = DOLR.substring(0,DOLR.indexOf(","));
        QUID = QUID.substring(0,QUID.indexOf(","));
        PENY = PENY.substring(0,PENY.indexOf("}"));
        exchangeRate.setSHIL(Float.parseFloat(SHIL));
        exchangeRate.setDOLR(Float.parseFloat(DOLR));
        exchangeRate.setQUID(Float.parseFloat(QUID));
        exchangeRate.setPENY(Float.parseFloat(PENY));


        return exchangeRate;
    }



    @Override
    protected void onPostExecute(ArrayList<ExchangeRate> result) {
        super.onPostExecute(result);
    }
}