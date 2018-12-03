package io.github.mikomw.coinz.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.mikomw.coinz.R;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import java.util.ArrayList;
import java.util.HashMap;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.user.User;
import io.github.mikomw.coinz.util.SerializableManager;
import io.github.mikomw.coinz.util.coinHelper;

public class displayCoinActivity extends AppCompatActivity {

    String tag = "displayCoinActivity";
    ArrayList<Coin> collectedCoin;
    ArrayList<Coin> spareChange;
    QMUIGroupListView mGroupListView;

    String currency;
    String shil = "SHIL";
    String dolr = "DOLR";
    String quid = "QUID";
    String peny = "PENY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_coin);
        currency = getIntent().getStringExtra("currency");

        Toolbar toolbar = (Toolbar) findViewById(R.id.display_coin_bar);
        toolbar.setTitle("My " + currency);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click");
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        collectedCoin =  (ArrayList<Coin>) getIntent().getSerializableExtra("collectCoin");
        spareChange =  (ArrayList<Coin>) getIntent().getSerializableExtra("spareChange");

        System.out.println(collectedCoin.size());

        toolbar.setTitle("My " + currency);

        mGroupListView=findViewById(R.id.group_list_coin_display);

        QMUIGroupListView.Section newSection = QMUIGroupListView.newSection(this);
        newSection.setTitle("Collected Coins");

        for(Coin coin : collectedCoin){
            QMUICommonListItemView temp = mGroupListView.createItemView(coin.getCurrency());
            temp.setDetailText(coin.getValue() + "");
            newSection.addItemView(temp,null);
        }
        newSection.addTo(mGroupListView);

        QMUIGroupListView.Section spareSection = QMUIGroupListView.newSection(this);
        spareSection.setTitle("Sparechange Coins");

        for(Coin coin : spareChange){
            QMUICommonListItemView temp = mGroupListView.createItemView(coin.getCurrency());
            temp.setDetailText(coin.getValue() + "");
            spareSection.addItemView(temp,null);
        }
        spareSection.addTo(mGroupListView);



    }

    @Override
    protected void onStart() {
        super.onStart();
    }



}
