package io.github.mikomw.coinz.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.rateDBOperator;
import io.github.mikomw.coinz.ui.activity.MainActivity;
import io.github.mikomw.coinz.util.Date;
import io.github.mikomw.coinz.util.DateInfo;
import io.github.mikomw.coinz.util.DownloadFileTask;
import io.github.mikomw.coinz.util.IO;
import io.github.mikomw.coinz.util.SharedPreferencesUtil;
import io.github.mikomw.coinz.util.UpdateRateTask;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class WelcomeActivity extends Activity {

    @BindView(R.id.iv_entry)
    ImageView mIVEntry;

    private static final int ANIM_TIME = 2000;

    private static final float SCALE_END = 1.15F;

    private static final int[] Imgs={
            R.drawable.me1,R.drawable.me2,
            R.drawable.me3,R.drawable.me4,
            R.drawable.me5,
            R.drawable.me6,R.drawable.me7,
            R.drawable.me8};

    DateInfo dateinfo;
    private rateDBOperator rateDBOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        // 如果是第一次启动，则先进入功能引导页
        //isFirstOpen = true;
        this.dateinfo = Date.getDateInfo();

        downloadTodayMap();
        downExchangeRate();

        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        startMainActivity();
    }
    private void startMainActivity(){
        Random random = new Random(SystemClock.elapsedRealtime());//SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
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

                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                WelcomeActivity.this.finish();
            }
        });
    }

    /**
     * 屏蔽物理返回按钮
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void downExchangeRate(){

        ArrayList<String> dates = dateinfo.month;
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("/data/user/0/io.github.mikomw.coinz/files/rate.db",null);
        rateDBOperator = new rateDBOperator(this);


        UpdateRateTask myTask = new UpdateRateTask();
        ArrayList<ExchangeRate>  result = null;



        ArrayList<String> updatedDates = new ArrayList<>();
        List<String> alreadyDone = rateDBOperator.queryAlllRate();
        for(String date : dates){
            if(!alreadyDone.contains(date)){
                System.out.println("Update" + date);
                updatedDates.add(date);
            }
        }

        try {
            result = myTask.execute(updatedDates).get();

        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
            e.printStackTrace();
        }

        if(result == null){
            System.out.println("UpdateTaskWRONG!");
        }else {
            for(ExchangeRate rate : result){
                if(!rateDBOperator.CheckIsDataAlreadyInDBorNot(rate.getDate()))
                    rateDBOperator.add(rate);
            }
        }



        // For debug information.
        /*
        List<ExchangeRate> temp =  rateDBOperator.queryMany();


        for(ExchangeRate ex : temp){
            System.out.println(ex.getDate());
            System.out.println(ex.getDOLR());
            System.out.println(ex.getPENY());
            System.out.println(ex.getQUID());
            System.out.println(ex.getSHIL());
        }
        */

    }


    private void downloadTodayMap() {
        String todayURL = dateinfo.todayURL;
        DownloadFileTask myTask = new DownloadFileTask();
        String result = null;
        try {
            result = myTask.execute(todayURL).get();
            IO.writeToFile(this.getFilesDir(),"todayMap.geojson",result);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException");
            e.printStackTrace();
        }
    }




}
