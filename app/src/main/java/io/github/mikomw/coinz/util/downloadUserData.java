package io.github.mikomw.coinz.util;


import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.rateDBOperator;
import io.github.mikomw.coinz.ui.activity.LoginActivity;
import io.github.mikomw.coinz.ui.activity.WelcomeActivity;
import io.github.mikomw.coinz.user.User;

public class downloadUserData extends AsyncTask<String, Void, Boolean> {

    private final WeakReference<Activity> weakActivity;
    private io.github.mikomw.coinz.db.rateDBOperator rateDBOperator;
    boolean isLogin;

    public downloadUserData(Activity myActivity) {
        this.weakActivity = new WeakReference<>(myActivity);
        isLogin = this.weakActivity.get() instanceof LoginActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String ... params) {

        String userID = params[0];
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference users = storageRef.child("users");
        StorageReference thisuser = users.child(userID);
        StorageReference todayCollectedRef = thisuser.child("todayCollectedCoinID.data");
        StorageReference collectedRef = thisuser.child("collectedCoin.data");
        StorageReference spareChangeRef = thisuser.child("spareChangeCoin.data");
        StorageReference userInfoRef = thisuser.child("userInfo.data");


        File todayCollectedCoinIDFile = new File(this.weakActivity.get().getFilesDir().getPath(),"todayCollectedCoinID.data");

        todayCollectedRef.getFile(todayCollectedCoinIDFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                System.out.println("DownLoad Success todayCollectedCoinID.data");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Fail to download todayCollectedCoinID.data");
                if(isLogin) {
                    SerializableManager.saveSerializable(weakActivity.get(), new HashSet<String>(), "todayCollectedCoinID.data");
                }
            }
        });

        File collectedCoinIDFile = new File(this.weakActivity.get().getFilesDir().getPath(),"collectedCoin.data");

        collectedRef.getFile(collectedCoinIDFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                System.out.println("DownLoad Success collectedCoin.data");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Fail to download collectedCoin.data");
                if(isLogin) {
                    SerializableManager.saveSerializable(weakActivity.get(), new ArrayList<Coin>(), "collectedCoin.data");
                }
            }
        });

        File spareChangeCoinFile = new File(this.weakActivity.get().getFilesDir().getPath(),"spareChangeCoin.data");

        spareChangeRef.getFile(spareChangeCoinFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                System.out.println("DownLoad Success spareChangeCoin.data");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Fail to download spareChangeCoin.data");
                if(isLogin)
                SerializableManager.saveSerializable(weakActivity.get(),new ArrayList<Coin>(),"spareChangeCoin.data");

            }
        });

        File userInfoFile = new File(this.weakActivity.get().getFilesDir().getPath(),"userInfo.data");

        userInfoRef.getFile(userInfoFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                System.out.println("DownLoad Success userInfo.data");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Fail to download userInfo.data");
                if(isLogin)
                    SerializableManager.saveSerializable(weakActivity.get(),new User(userID),"userInfo.data");

            }
        });

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Activity activity = this.weakActivity.get();
        if(activity instanceof LoginActivity){
            ((LoginActivity) activity).jumpToMap();
        }
    }
}




