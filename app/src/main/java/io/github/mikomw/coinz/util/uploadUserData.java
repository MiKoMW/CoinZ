package io.github.mikomw.coinz.util;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
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
import java.util.List;

import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.rateDBOperator;
import io.github.mikomw.coinz.ui.activity.SignupActivity;
import io.github.mikomw.coinz.ui.activity.WelcomeActivity;

public class uploadUserData extends AsyncTask<String, Void, Boolean> {

    private final WeakReference<Activity> weakActivity;
    private io.github.mikomw.coinz.db.rateDBOperator rateDBOperator;

    public uploadUserData(Activity myActivity) {
        this.weakActivity = new WeakReference<>(myActivity);
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

        Uri file = Uri.fromFile(new File(this.weakActivity.get().getFilesDir().getPath(),"todayCollectedCoinID.data"));
        UploadTask uploadtodayCollected = todayCollectedRef.putFile(file);
        uploadtodayCollected.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Fail to upload todayCollectedCoin.data");
                // Handle unsuccessful uploads
                SerializableManager.saveSerializable(weakActivity.get(),new ArrayList<String>(),"todayCollectedCoinID.data");
                todayCollectedRef.putFile(file);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("Success");

            }
        });

        Uri collected = Uri.fromFile(new File(this.weakActivity.get().getFilesDir().getPath(),"collectedCoin.data"));
        UploadTask uploadCollected = collectedRef.putFile(collected);
        uploadCollected.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Fail to upload collectedCoin.data");
                // Handle unsuccessful uploads
                SerializableManager.saveSerializable(weakActivity.get(),new ArrayList<Coin>(),"collectedCoin.data");
                collectedRef.putFile(collected);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("Success");

            }
        });

        Uri spareChange = Uri.fromFile(new File(this.weakActivity.get().getFilesDir().getPath(),"spareChange.data"));
        UploadTask uploadSpareChange = spareChangeRef.putFile(spareChange);
        uploadSpareChange.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Fail to upload spareChange.data");
                SerializableManager.saveSerializable(weakActivity.get(),new ArrayList<Coin>(),"spareChange.data");
                spareChangeRef.putFile(spareChange);
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("Success");

            }
        });


        Uri userInfo = Uri.fromFile(new File(this.weakActivity.get().getFilesDir().getPath(),"userInfo.data"));
        UploadTask uploadUserInfo = userInfoRef.putFile(userInfo);
        uploadUserInfo.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Fail to upload userInfo.data");
                // Handle unsuccessful uploads
                SerializableManager.saveSerializable(weakActivity.get(),new ArrayList<Coin>(),"userInfo.data");
                userInfoRef.putFile(userInfo);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("Success");

            }
        });


        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        Activity activity = this.weakActivity.get();
        if(activity instanceof SignupActivity){
            ((SignupActivity) activity).jumpToMap();
        }

    }
}




