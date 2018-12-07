package io.github.mikomw.coinz.ui.activity;



import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.user.User;
import io.github.mikomw.coinz.util.SerializableManager;
import io.github.mikomw.coinz.util.uploadUserData;

/**
 * The map activity enables our players to pay coins into the central bank.
 * This activity consists four fragment which will show the exchange rate information for all four currencies.
 *
 * @author Songbo Hu
 */
public class MarketActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    String tag = "MarketActivity";

    String Uid;
    User user;

    TextView UidTextView;

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

                fragmentManager.executePendingTransactions();

            }


            if (fragment.getView().getParent() == null) {
                container.addView(fragment.getView());  // Get view for viewpager.
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
            container.removeView(mPages.get(Pager.getPagerFromPositon(position)).getView()); // remove the layout of other pages.
        }
    };


    private void initPagers() {
        mPages = new HashMap<>();

        // Set pages for for currency.
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

        // Initialize the tool bar.
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

        // Initialize the navigation view.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Set listener.
        navigationView.setNavigationItemSelectedListener(this);


        Uid = getIntent().getStringExtra("Uid");
        View headerView = navigationView.getHeaderView(0);
        userNickName = headerView.findViewById(R.id.drawer_NikeName);
        UidTextView = headerView.findViewById(R.id.drawer_email);
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
        Log.d(tag, "Is user info file exsisting : " + newFile.exists());
        user = SerializableManager.readSerializable(this,"userInfo.data");

        this.Uid = user.getUID();
        initUserView();
        mTabSegment = findViewById(R.id.tabSegment);
        mContentViewPager = findViewById(R.id.contentViewPager);

            QMUITabSegment.Tab peny_tab = new QMUITabSegment.Tab("PENY");
            QMUITabSegment.Tab shil_tab = new QMUITabSegment.Tab("SHIL");
            QMUITabSegment.Tab dolr_tab = new QMUITabSegment.Tab("DOLR");
            QMUITabSegment.Tab quid_tab = new QMUITabSegment.Tab("QUID");

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
                    Log.i(tag, "Select tab index : " + String.valueOf(index));
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




    // Navigation menu listener.
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
        UidTextView.setText(user.getUID());
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
            intent.putExtra("Uid",Uid);
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
            Intent intent = new Intent(this, FriendActivity.class);
            startActivity(intent);
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

