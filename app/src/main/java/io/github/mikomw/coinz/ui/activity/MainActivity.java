package io.github.mikomw.coinz.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.util.Miko;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();



        SharedPreferences mPrefs=this.getSharedPreferences(this.getApplicationInfo().name, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=mPrefs.edit();
        Gson gson = new Gson();
        ed.putString("myObjectKey", gson.toJson(new Miko(1,2)));
        ed.commit();




    }

    public String readJson(){
        ArrayList<LatLng> points = new ArrayList<>();

        try {
            // Load GeoJSON file
            FileInputStream inputStream = openFileInput("todayMap.geojson");
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            inputStream.close();

            // Parse JSON
            JSONObject json = new JSONObject(sb.toString());
            JSONObject rates = json.getJSONObject("rates");
            System.out.println(rates);
            System.out.println((rates).getDouble("SHIL"));

            JSONArray coins = json.getJSONArray("features");


            /*
            for(int con = 0; con <coins.length(); con++){
                System.out.println(coins.getJSONObject(con));
            }*/

            JSONObject coin = coins.getJSONObject(0);
            System.out.println(coin.getJSONObject("properties").getString("id"));




            return sb.toString();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return "mei";
    }

}

