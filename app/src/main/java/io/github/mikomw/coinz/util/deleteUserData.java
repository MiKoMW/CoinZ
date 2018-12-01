package io.github.mikomw.coinz.util;

import android.app.Activity;

import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;

public class deleteUserData {

    private final WeakReference<Activity> weakActivity;

    public deleteUserData(Activity myActivity) {
        this.weakActivity = new WeakReference<>(myActivity);
    }

    public void delete(){

        SerializableManager.removeSerializable(this.weakActivity.get(),"todayCollectedCoinID.data");
        SerializableManager.removeSerializable(this.weakActivity.get(),"collectedCoin.data");
        SerializableManager.removeSerializable(this.weakActivity.get(),"spareChangeCoin.data");
        SerializableManager.removeSerializable(this.weakActivity.get(),"userInfo.data");
    }


}
