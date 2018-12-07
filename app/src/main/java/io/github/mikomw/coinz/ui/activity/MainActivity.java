package io.github.mikomw.coinz.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.util.downloadUserData;

// This activity is for temporary testing purpose.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadUserData task = new downloadUserData(this);
        task.execute("100086");

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
            for(int con = 0; con <todayCoins.length(); con++){
                System.out.println(todayCoins.getJSONObject(con));
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

