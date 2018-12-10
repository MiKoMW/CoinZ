package io.github.mikomw.coinz.util;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * A helper class to help us delete all the previous user information.
 *
 * @author Songbo Hu
 */
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
