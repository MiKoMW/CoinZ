package io.github.mikomw.coinz.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
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
import io.github.mikomw.coinz.util.deleteUserData;
import io.github.mikomw.coinz.util.downloadUserData;
import io.github.mikomw.coinz.util.uploadUserData;

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

