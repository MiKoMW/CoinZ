package io.github.mikomw.coinz.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.adapter.GuideViewPagerAdapter;
import io.github.mikomw.coinz.util.SharedPreferencesUtil;

/**
 *
 * A welcome guide in order to enhance the user experience. In this activity, a three pages user guide
 * on how to play this game will be shown. This activity will be shown at the first time user open this game.
 * Or user could read this guide by pressing the How to play button on the drawer in the major game activities.
 *
 * @author Songbo Hu
 * @author xialo
 */


public class WelcomeGuideActivity extends Activity implements View.OnClickListener {
    String tag = "WelcomeGuideActivity";

    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button startBtn;

    // The image resource of the guide pages.
    private static final int[] pics = { R.layout.guide_view1,
            R.layout.guide_view2, R.layout.guide_view3};

    // The bottom dots images
    private ImageView[] dots;

    // Current index of screens
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        views = new ArrayList<View>();

        // Initialize the list view.
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);

            if (i == pics.length - 1) {
                startBtn = (Button) view.findViewById(R.id.btn_enter);
                startBtn.setTag("enter");
                startBtn.setOnClickListener(this);
            }
            views.add(view);
        }

        vp = (ViewPager) findViewById(R.id.vp_guide);
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new PageChangeListener());

        initDots();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // In case of switching to background, do not show the guide again.
        SharedPreferencesUtil.putBoolean(WelcomeGuideActivity.this, SharedPreferencesUtil.FIRST_OPEN, false);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];

        for (int i = 0; i < pics.length; i++) {

            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true);

    }

    // Set the current view.
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    // Set the current Dot to the current view.
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }

        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterMainActivity() {
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);

        if(!isFirstOpen){
            // In the case, the guide is entered from other activities.
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
        }else {

            Intent intent = new Intent(WelcomeGuideActivity.this,
                    WelcomeActivity.class);
            startActivity(intent);
        }
        SharedPreferencesUtil.putBoolean(WelcomeGuideActivity.this, SharedPreferencesUtil.FIRST_OPEN, false);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int position) {

        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            setCurDot(position);
        }

    }
}
