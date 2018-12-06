package io.github.mikomw.coinz.util;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;

import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.ui.activity.SignupActivity;
import io.github.mikomw.coinz.user.User;

public class uploadUserData extends AsyncTask<String, Void, Boolean> {

    private final static String tag = "uploadUserDataTask";

    private final WeakReference<Activity> weakActivity;

    // To check if login activity called this task
    // If it is, we need to jump to map activity after this task.
    private boolean isSignUp;

    // We counter how many download has finished.
    // We need all four finished before we jump to other activity to avoid null pointer exception.
    private int jump_counter;

    private QMUITipDialog tipDialog;

    public uploadUserData(Activity myActivity) {
        this.weakActivity = new WeakReference<>(myActivity);
        this.isSignUp = (this.weakActivity.get() instanceof SignupActivity);
        tipDialog = new QMUITipDialog.Builder(this.weakActivity.get())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("Synchronize your data.")
                .create();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        jump_counter = 0;
        if(isSignUp)
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
        StorageReference spareChangeRef = cur_user.child("spareChangeCoin.data");
        StorageReference userInfoRef = cur_user.child("userInfo.data");

        // Upload the user data file.
        // In case of success, jump to the map activity or do it in background.
        // In case of failure, do nothing. Our app will guarantee to have the local file.
        // This should not normally happen.
        Uri file = Uri.fromFile(new File(this.weakActivity.get().getFilesDir().getPath(),"todayCollectedCoinID.data"));
        UploadTask uploadtodayCollected = todayCollectedRef.putFile(file);
        uploadtodayCollected.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(tag,"Fail to upload todayCollectedCoin.data");
                // Initialize data for new user.
                if(isSignUp){
                    Log.d(tag,"New file created for todayCollectedCoin.data");
                    SerializableManager.saveSerializable(weakActivity.get(),new HashSet<String>(),"todayCollectedCoinID.data");
                    todayCollectedRef.putFile(file);
                }

                jumpToActivity();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload Success");
                jumpToActivity();
            }
        });

        Uri collected = Uri.fromFile(new File(this.weakActivity.get().getFilesDir().getPath(),"collectedCoin.data"));
        UploadTask uploadCollected = collectedRef.putFile(collected);
        uploadCollected.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(tag,"Fail to upload collectedCoin.data");
                // Handle unsuccessful uploads
                if(isSignUp){
                    Log.d(tag,"New file created for collectedCoin.data");
                    SerializableManager.saveSerializable(weakActivity.get(),new ArrayList<Coin>(),"collectedCoin.data");
                    collectedRef.putFile(collected);
                }

                jumpToActivity();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                System.out.println("Upload Success");
                jumpToActivity();

            }
        });

        Uri spareChange = Uri.fromFile(new File(this.weakActivity.get().getFilesDir().getPath(),"spareChange.data"));
        UploadTask uploadSpareChange = spareChangeRef.putFile(spareChange);
        uploadSpareChange.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(tag,"Fail to upload spareChange.data");
                if (isSignUp) {
                    Log.d(tag,"New file created for spareChange.data");

                    SerializableManager.saveSerializable(weakActivity.get(),new ArrayList<Coin>(),"spareChange.data");
                    spareChangeRef.putFile(spareChange);
                }
                jumpToActivity();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                System.out.println("Upload Success");
                jumpToActivity();


            }
        });


        Uri userInfo = Uri.fromFile(new File(this.weakActivity.get().getFilesDir().getPath(),"userInfo.data"));
        UploadTask uploadUserInfo = userInfoRef.putFile(userInfo);
        uploadUserInfo.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(tag,"Fail to upload userInfo.data");
                if(isSignUp){
                    Log.d(tag,"New file created for userInfo.data");
                    SerializableManager.saveSerializable(weakActivity.get(),new User(userID),"userInfo.data");
                    userInfoRef.putFile(userInfo);
                }
                jumpToActivity();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("Upload Success");
                jumpToActivity();
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
        if(isSignUp) {
            tipDialog.dismiss();
            Activity activity = this.weakActivity.get();
            if (activity instanceof SignupActivity) {
                ((SignupActivity) activity).jumpToMap();
            }
        }
    }
}




