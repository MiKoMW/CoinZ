package io.github.mikomw.coinz.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.mikomw.coinz.R;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.Objects;

import io.github.mikomw.coinz.coin.Coin;

/**
 * A display coin activity which will show the coin list with a selected currency.
 *
 * @author Songbo Hu
 */
public class DisplayCoinActivity extends AppCompatActivity {

    private static final String tag = "DisplayCoinActivity";
    private ArrayList<Coin> collectedCoin;
    private ArrayList<Coin> spareChange;
    private QMUIGroupListView mGroupListView;

    private String currency;
    private static final String shil = "SHIL";
    private static final String dolr = "DOLR";
    private static final String quid = "QUID";
    private static final String peny = "PENY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_coin);

        // Check which currency are we going to display.
        currency = getIntent().getStringExtra("currency");

        Log.d(tag,"Display coins activity with " + currency);

        // Initialize the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.display_coin_bar);
        toolbar.setTitle("My " + currency);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

        // Get the coins passing by the previous activity.
        collectedCoin = (ArrayList<Coin>) getIntent().getSerializableExtra("collectCoin");
        spareChange = (ArrayList<Coin>) getIntent().getSerializableExtra("spareChange");

        Log.d(tag,"The collected coin size is: " + collectedCoin.size());
        Log.d(tag,"The spareChange coin size is: " + spareChange.size());

        // Initialize the list view.
        mGroupListView=findViewById(R.id.group_list_coin_display);

        // Create a onclick listener for the list view.
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {

                    QMUICommonListItemView viewList = (QMUICommonListItemView) v;
                    Log.d(tag, "Coin selected with value " + viewList.getDetailText().toString());
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    int this_coinid = viewList.getId();
                    System.out.println(this_coinid);
                    if(text.toString().contains("Spare")){
                        Log.d(tag, "Coin selected ：" + spareChange.get(this_coinid));
                        Intent intent = new Intent();
                        intent.putExtra("result", spareChange.get(this_coinid));
                        intent.putExtra("collected", false);

                        // Pass the coin back to the calling activity.
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    if(text.toString().contains("Collected")){
                        Log.d(tag, "Coin selected ：" + collectedCoin.get(this_coinid));
                        Intent intent = new Intent();
                        intent.putExtra("result", collectedCoin.get(this_coinid));
                        intent.putExtra("collected", true);

                        // Pass the coin back to the calling activity.
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }
            }
        };


        // Initialize the list view.
        QMUIGroupListView.Section newSection = QMUIGroupListView.newSection(this);
        newSection.setTitle("Collected Coins");

        int con = 0;
        for(Coin coin : collectedCoin){
            QMUICommonListItemView temp = mGroupListView.createItemView("Collected Coin: " + con);
            temp.setId(con);
            temp.setDetailText(coin.getValue() + "");
            newSection.addItemView(temp,onClickListener);
            con++;
        }
        newSection.addTo(mGroupListView);

        QMUIGroupListView.Section spareSection = QMUIGroupListView.newSection(this);
        spareSection.setTitle("Sparechange Coins");


        con = 0;
        for(Coin coin : spareChange){
            QMUICommonListItemView temp = mGroupListView.createItemView("Spare Coin: " + con);
            temp.setId(con);
            temp.setDetailText(coin.getValue() + "");
            spareSection.addItemView(temp,onClickListener);
            con++;
        }
        spareSection.addTo(mGroupListView);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // On back button pressed, we go back to our previous activity.
    @Override
    public void onBackPressed() {
        System.out.println("Click");
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
        super.onBackPressed();

    }
}