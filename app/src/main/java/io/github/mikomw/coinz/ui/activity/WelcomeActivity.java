package io.github.mikomw.coinz.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.rateDBOperator;
import io.github.mikomw.coinz.util.Date;
import io.github.mikomw.coinz.util.DateInfo;
import io.github.mikomw.coinz.util.IO;
import io.github.mikomw.coinz.util.SerializableManager;
import io.github.mikomw.coinz.util.SharedPreferencesUtil;
import io.github.mikomw.coinz.util.deleteUserData;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class WelcomeActivity extends Activity {

    String tag = "WelcomeActivity";

    @BindView(R.id.iv_entry)
    ImageView mIVEntry;

    private static final int ANIM_TIME = 2000;

    private static final float SCALE_END = 1.15F;

    private int finished;

    private static final int[] Imgs={
            R.drawable.me1,R.drawable.me2,
            R.drawable.me3,R.drawable.me4,
            R.drawable.me5,
            R.drawable.me6,R.drawable.me7,
            R.drawable.me8};


    DateInfo dateinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if it is the first time for the user to use this app.
        // If so, enter the help activity.
        // isFirstOpen = true;
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);

        // For the call back function to know if other tasks are finished.
        finished = 0;
        this.dateinfo = Date.getDateInfo();

        // Download today's map
        DownloadFileTask myTask = new DownloadFileTask(this);
        myTask.execute(dateinfo.todayURL);

        // Download past exchange rates.
        UpdateRateTask updatedRates = new UpdateRateTask(this);
        updatedRates.execute();

        // Delete previous user data.
        deleteUserData deleteUserData = new deleteUserData(this);
        deleteUserData.delete();


        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // If not the first time open this app, go to login normally.
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        startMainActivity();
    }

    private void startMainActivity(){
        //SystemClock.elapsedRealtime() random seed for choosing the initial page.

        Random random = new Random(SystemClock.elapsedRealtime());
        mIVEntry.setImageResource(Imgs[random.nextInt(Imgs.length)]);

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>()
                {

                    @Override
                    public void call(Long aLong)
                    {
                        startAnim();
                    }
                });

    }

    private void startAnim() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mIVEntry, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mIVEntry, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter()
        {

            @Override
            public void onAnimationEnd(Animator animation)
            {
                jumpToLogin();
            }
        });
    }

    // A call back function to make sure that all the processes are finished.
    // 1 The loading map
    // 2 The updating exchange rate
    // 3 The welcome animation
    public void jumpToLogin(){
        this.finished = finished + 1;
        System.out.println(finished);
        if(finished == 3){
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            WelcomeActivity.this.finish();
        }
    }


     //Shield Physical Return Button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class DownloadFileTask extends AsyncTask<String, Void, String> {
        private static final String tag = "DownloadFileTask";
        private final WeakReference<Activity> weakActivity;

        DownloadFileTask(Activity myActivity) {
            this.weakActivity = new WeakReference<>(myActivity);
        }
        @Override
        protected String doInBackground(String... urls) {
            Log.i(tag, "Download today map.");
            try {
                return loadFileFromNetwork(urls[0]);
            } catch (IOException e) {
                return "Unable to load content. Check network connection.";
            }
        }

        private String loadFileFromNetwork(String urlString) throws IOException {
            return readStream(downloadUrl(new URL(urlString)));
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
        private String readStream(InputStream stream) throws IOException {

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            IO.writeToFile(this.weakActivity.get().getFilesDir().getPath(),"todayMap.geojson",result);

            ArrayList<Coin> todaycoins = new ArrayList<>();
            try {
                FeatureCollection coins = FeatureCollection.fromJson(result);
                if(coins == null){
                    return;
                }
                for (Feature feature : coins.features()) {
                    // Coin features ==============================================//
                    String id = feature.getStringProperty("id");
                    Double value = Double.parseDouble(feature.getStringProperty("value"));
                    String currency = feature.getStringProperty("currency");
                    Point point = (Point) feature.geometry();
                    if(point == null){
                        Log.e(tag,"A coin with null position. Parsing error.");
                        continue;
                    }
                    LatLng latLng = new LatLng(point.latitude(), point.longitude());

                    // Creating coin object
                    Coin coin = new Coin(id,value,currency,latLng);
                    todaycoins.add(coin);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            // Save coins to the local data file.
            SerializableManager.saveSerializable(this.weakActivity.get(),todaycoins,"todayCoins.coin");

            Log.i(tag, "Download today's coin succeed.");
            // Call back function.
            ((WelcomeActivity) weakActivity.get()).jumpToLogin();
        }
    }


    public static class UpdateRateTask extends AsyncTask<Void, Void, ArrayList<ExchangeRate>>{
        private static final String tag = "UpdateRateTask";

        private final WeakReference<Activity> weakActivity;
        private rateDBOperator rateDBOperator;

        UpdateRateTask(Activity myActivity) {
            this.weakActivity = new WeakReference<>(myActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(tag, "Download exchange rate.");
            rateDBOperator = new rateDBOperator(this.weakActivity.get());
            Log.v(tag, "Datebase operator initialization.");

        }

        @Override
        protected ArrayList<ExchangeRate> doInBackground(Void ... params) {
            ArrayList<String> updatedDates = new ArrayList<>();
            List<String> alreadyDone = rateDBOperator.queryAlllRate();

            // Check if we have already updated for the information.
            ArrayList<String> dates = Date.getDateInfo().month;

            for(String date : dates){
                if(!alreadyDone.contains(date)){
                    System.out.println("Update" + date);
                    updatedDates.add(date);
                }
            }

            ArrayList<ExchangeRate> ans = new ArrayList<>();
            try {
                for(String date : updatedDates){
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

            try {
                JSONObject json = new JSONObject(temp);
                JSONObject rates = json.getJSONObject("rates");
                Double SHIL = (rates).getDouble("SHIL");
                Double DOLR = (rates).getDouble("DOLR");
                Double QUID = (rates).getDouble("QUID");
                Double PENY = (rates).getDouble("PENY");

                exchangeRate.setSHIL(SHIL);
                exchangeRate.setDOLR(DOLR);
                exchangeRate.setQUID(QUID);
                exchangeRate.setPENY(PENY);
            } catch (Exception e){
                e.printStackTrace();
            }




            return exchangeRate;
        }



        @Override
        protected void onPostExecute(ArrayList<ExchangeRate> result) {
            super.onPostExecute(result);
            Activity activity = weakActivity.get();
            if (activity == null
                    || activity.isFinishing()
                    || activity.isDestroyed()) {
                // activity is no longer valid, don't do anything!
                return;
            }


            // Store the rates in the SQLite database.
            for(ExchangeRate rate : result){
                if(!rateDBOperator.CheckIsDataAlreadyInDBorNot(rate.getDate()))
                    rateDBOperator.add(rate);
            }

            // Call back function.
            ((WelcomeActivity) activity).jumpToLogin();


        }
    }




}
