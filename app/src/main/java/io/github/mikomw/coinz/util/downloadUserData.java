package io.github.mikomw.coinz.util;


import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;

import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.ui.activity.LoginActivity;
import io.github.mikomw.coinz.user.User;

public class downloadUserData extends AsyncTask<String, Void, Boolean> {
    private final static String tag = "downloadUserDataTask";

    private final WeakReference<Activity> weakActivity;

    // To check if login activity called this task
    // If it is, we need to jump to map activity after this task.
    private boolean isLogin;

    // We counter how many download has finished.
    // We need all four finished before we jump to other activity to avoid null pointer exception.
    private int jump_counter;

    private QMUITipDialog  tipDialog;


    public downloadUserData(Activity myActivity) {
        this.weakActivity = new WeakReference<>(myActivity);
        isLogin = this.weakActivity.get() instanceof LoginActivity;
        tipDialog = new QMUITipDialog.Builder(this.weakActivity.get())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("Loading your data.")
                .create();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        jump_counter = 0;
        tipDialog.show();
    }

    @Override
    protected Boolean doInBackground(String ... params) {

        // Pass user ID.
        String userID = params[0];

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference users = storageRef.child("users");
        StorageReference cur_user = users.child(userID);
        StorageReference todayCollectedRef = cur_user.child("todayCollectedCoinID.data");
        StorageReference collectedRef = cur_user.child("collectedCoin.data");
        StorageReference spareChangeRef = cur_user.child("spareChange.data");
        StorageReference userInfoRef = cur_user.child("userInfo.data");


        File todayCollectedCoinIDFile = new File(this.weakActivity.get().getFilesDir().getPath(),"todayCollectedCoinID.data");

        // Download the user data file.
        // In case of success, jump to the map activity or jump back.
        // In case of failure, we create new file.
        // This should not normally happen.
        todayCollectedRef.getFile(todayCollectedCoinIDFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(tag,"DownLoad Success todayCollectedCoinID.data");
                jumpToActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(tag,"Fail to download todayCollectedCoinID.data");
                if(isLogin) {
                    SerializableManager.saveSerializable(weakActivity.get(), new HashSet<String>(), "todayCollectedCoinID.data");
                    jumpToActivity();
                }
            }
        });

        File collectedCoinFile = new File(this.weakActivity.get().getFilesDir().getPath(),"collectedCoin.data");

        collectedRef.getFile(collectedCoinFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(tag,"DownLoad Success collectedCoin.data");
                jumpToActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(tag,"Fail to download collectedCoin.data");
                if(isLogin) {
                    SerializableManager.saveSerializable(weakActivity.get(), new ArrayList<Coin>(), "collectedCoin.data");
                    jumpToActivity();
                }
            }
        });

        File spareChangeFile = new File(this.weakActivity.get().getFilesDir().getPath(),"spareChange.data");

        spareChangeRef.getFile(spareChangeFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(tag,"DownLoad Success spareChange.data");
                jumpToActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(tag,"Fail to download spareChange.data");
                if(isLogin) {
                    SerializableManager.saveSerializable(weakActivity.get(), new ArrayList<Coin>(), "spareChange.data");
                    jumpToActivity();
                }
            }
        });

        File userInfoFile = new File(this.weakActivity.get().getFilesDir().getPath(),"userInfo.data");

        userInfoRef.getFile(userInfoFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(tag,"DownLoad Success userInfo.data");
                jumpToActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(tag,"Fail to download userInfo.data");
                if(isLogin) {
                    SerializableManager.saveSerializable(weakActivity.get(), new User(userID), "userInfo.data");
                    jumpToActivity();
                }
            }
        });

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        jumpToActivity();
    }


    private void jumpToActivity(){
        jump_counter++;
        System.out.println(jump_counter);

        // We will wait the fifth call of this function.
        if(jump_counter <= 4){
            System.out.println("");
            return;
        }
        tipDialog.dismiss();
        Activity activity = this.weakActivity.get();
        if(activity instanceof LoginActivity){
            ((LoginActivity) activity).jumpToMap();
        }
    }

}




