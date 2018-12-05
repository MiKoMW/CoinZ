package io.github.mikomw.coinz.ui.activity;


import android.app.Fragment;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;

import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import butterknife.BindView;
import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.user.User;
import io.github.mikomw.coinz.util.SerializableManager;
import io.github.mikomw.coinz.util.uploadUserData;




public class MarketActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    String tag = "MarketActivity";
    private PermissionsManager permissionsManager;

    ArrayList<Coin> todayCoins;
    HashMap<String,Marker> todayIcons;

    //=================================================这个以后得改
    HashSet<String> todayCollectedID;
    ArrayList<Coin> CollectedCoins;
    String Uid;
    User user;

    TextView userNickName;
    ImageView avater;
    DrawerLayout drawer;
    QMUITabSegment mTabSegment;
    ViewPager mContentViewPager;


    Map<Pager, android.support.v4.app.Fragment> mPages;
    FragmentManager fragmentManager;
    PagerAdapter adapter = new PagerAdapter() {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            android.support.v4.app.Fragment fragment = mPages.get(Pager.getPagerFromPositon(position));
            if (!fragment.isAdded()) { // 如果fragment还没有added
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(fragment, fragment.getClass().getSimpleName());
                ft.commit();
                /**
                 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
                 * 会在进程的主线程中，用异步的方式来执行。
                 * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
                 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
                 */
                fragmentManager.executePendingTransactions();

            }


            if (fragment.getView().getParent() == null) {
                container.addView(fragment.getView()); // 为viewpager增加布局
            }
            return fragment.getView();
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mPages.get(Pager.getPagerFromPositon(position)).getView()); // 移出viewpager两边之外的page布局
        }
    };


    private void initPagers() {
        mPages = new HashMap<>();



        mPages.put(Pager.PENY, MarketFragment.newInstance("PENY"));
        mPages.put(Pager.DOLR, MarketFragment.newInstance("DOLR"));
        mPages.put(Pager.SHIL, MarketFragment.newInstance("SHIL"));
        mPages.put(Pager.QUID, MarketFragment.newInstance("QUID"));

        mContentViewPager.setAdapter(adapter);
        mTabSegment.setupWithViewPager(mContentViewPager, false);


    }


    enum Pager {
        PENY, DOLR , SHIL, QUID;

        public static Pager getPagerFromPositon(int position) {
            switch (position) {
                case 0:
                    return PENY;
                case 1:
                    return DOLR;
                case 2:
                    return SHIL;
                case 3:
                    return QUID;
                default:
                    return PENY;
            }
        }
    }




        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.account);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.menu));



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //设置navigationview的menu监听
        navigationView.setNavigationItemSelectedListener(this);


        Uid = getIntent().getStringExtra("Uid");
        //userName.setText(Uid);
        View headerView = navigationView.getHeaderView(0);
        userNickName = headerView.findViewById(R.id.drawer_NikeName);
        userNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("USerNameClick");
                final QMUIDialog.EditTextDialogBuilder builder =new QMUIDialog.EditTextDialogBuilder(MarketActivity     .this);
                builder.setTitle("Set your nick name!").setPlaceholder("Please enter your nick name.")
                        .setInputType(InputType.TYPE_CLASS_TEXT).addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }).addAction("Rename!", new QMUIDialogAction.ActionListener() {

                    @Override            public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text =builder.getEditText().getText();
                        if (text !=null && text.length() >0)
                        {
                            Toast.makeText(MarketActivity.this, "Your nick name is: " + text, Toast.LENGTH_SHORT).show();
                            MarketActivity.this.user.setNickName(text.toString());
                            userNickName.setText(user.getNickName());
                            SerializableManager.saveSerializable(MarketActivity.this,user,"userInfo.data");
                            saveData();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(MarketActivity.this, "Please enter your nick name.", Toast.LENGTH_SHORT).show();
                        }}}).show();

            }
        });
        File newFile = new File(getFilesDir().getPath()+"/userInfo.data");
        System.out.println(newFile.exists());
        user = SerializableManager.readSerializable(this,"userInfo.data");

        this.Uid = user.getUID();
        initUserView();
        mTabSegment = findViewById(R.id.tabSegment);
        mContentViewPager = findViewById(R.id.contentViewPager);

            QMUITabSegment.Tab peny_tab = new QMUITabSegment.Tab(
                    //ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_tabbar_component),
                    //ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_tabbar_component_selected),
                    "PENY"
            );

            QMUITabSegment.Tab shil_tab = new QMUITabSegment.Tab(
                    //ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_tabbar_util),
                    //ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_tabbar_util_selected),
                    "SHIL"
            );
            QMUITabSegment.Tab dolr_tab = new QMUITabSegment.Tab(
                    //ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_tabbar_lab),
                    //ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_tabbar_lab_selected),
                    "DOLR"
            );
            QMUITabSegment.Tab quid_tab = new QMUITabSegment.Tab(
                    //ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_tabbar_lab),
                    //ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon_tabbar_lab_selected),
                    "QUID"
            );

            mTabSegment.addTab(peny_tab)
                    .addTab(dolr_tab)
                    .addTab(shil_tab)
                    .addTab(quid_tab);
            mTabSegment.notifyDataChanged();
            fragmentManager = getSupportFragmentManager();
            initPagers();

            mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
                @Override
                public void onTabSelected(int index) {
                    Log.i("sexxx", String.valueOf(index));
                    mContentViewPager.setCurrentItem(index);
                }

                @Override
                public void onTabUnselected(int index) {

                }

                @Override
                public void onTabReselected(int index) {

                }

                @Override
                public void onDoubleTap(int index) {

                }
            });

    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wallet) {
            System.out.println("Wallet Sellected");
            saveData();
            Intent intent = new Intent(this, WalletActivity.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_guide) {
            System.out.println("Guide Sellected");
            saveData();
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivityForResult(intent,0);

        } else if (id == R.id.nav_setting) {
            System.out.println("setting selected;");
            saveData();
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent,0);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initUserView(){
        userNickName.setText(user.getNickName());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_explore) {
            System.out.println("Explore");
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            System.out.println("Explore");
            return true;
        }

        if (id == R.id.action_market) {
            System.out.println("Market");
            //startActivity(intent);
            return true;
        }

        if (id == R.id.action_friend) {
            System.out.println("Friend");
            Intent intent = new Intent(this, WalletActivity.class);
            //startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void saveData(){

        uploadUserData uploadUserData = new uploadUserData(this);
        uploadUserData.execute(this.Uid);

    }

}
