package io.github.mikomw.coinz.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import java.util.ArrayList;
import java.util.HashMap;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.user.User;
import io.github.mikomw.coinz.util.SerializableManager;
import io.github.mikomw.coinz.util.coinHelper;

/**
 * WalletActivity will show the coin and gold in user's wallet.
 * User can see coins in four currencies in detail. User could not make any changes or deals in this page.
 *
 * @author Songbo Hu
 */

public class WalletActivity extends AppCompatActivity {

    String tag = "WalletActivity";
    HashMap<String,ArrayList<Coin>> collectedCoins;
    HashMap<String,ArrayList<Coin>> spareChanges;

    User user;
    QMUIGroupListView mGroupListView;
    String shil = "SHIL";
    String dolr = "DOLR";
    String quid = "QUID";
    String peny = "PENY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        collectedCoins = new HashMap<>();
        spareChanges = new HashMap<>();

        // Read the file from the local data.
        ArrayList<Coin> spare_temp = SerializableManager.readSerializable(this,"spareChange.data");
        ArrayList<Coin> collected_temp = SerializableManager.readSerializable(this,"collectedCoin.data");
        user = SerializableManager.readSerializable(this,"userInfo.data");

        // Sort the coins into four currencies
        ArrayList<Coin> temp_list;

        collectedCoins.put(shil,new ArrayList<>());
        collectedCoins.put(dolr,new ArrayList<>());
        collectedCoins.put(quid,new ArrayList<>());
        collectedCoins.put(peny,new ArrayList<>());

        spareChanges.put(shil,new ArrayList<>());
        spareChanges.put(dolr,new ArrayList<>());
        spareChanges.put(quid,new ArrayList<>());
        spareChanges.put(peny,new ArrayList<>());


        for(Coin coin : collected_temp){
            temp_list = collectedCoins.getOrDefault(coin.getCurrency(),new ArrayList<>());
            temp_list.add(coin);
            collectedCoins.put(coin.getCurrency(),temp_list);
        }

        for(Coin coin : spare_temp){
            temp_list = spareChanges.getOrDefault(coin.getCurrency(),new ArrayList<>());
            temp_list.add(coin);
            spareChanges.put(coin.getCurrency(),temp_list);
        }

        // Initialize the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.wallet_bar);

        toolbar.setTitle("My Wallet");
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

        // Initialize the list view.
        mGroupListView=findViewById(R.id.group_list_item_contact);

        QMUICommonListItemView listItemName = mGroupListView.createItemView("Gold Balance");
        listItemName.setDetailText(user.getBalance().toString());
        listItemName.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);



        QMUICommonListItemView yourPeny = mGroupListView.createItemView(peny);
        yourPeny.setDetailText((coinHelper.sumCoinValue(spareChanges.get(peny)) + coinHelper.sumCoinValue(collectedCoins.get(peny)))+"");
        yourPeny.setImageDrawable(getResources().getDrawable(R.drawable.peny));

        yourPeny.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView yourDolr = mGroupListView.createItemView(dolr);
        yourDolr.setDetailText((coinHelper.sumCoinValue(spareChanges.get(dolr)) + coinHelper.sumCoinValue(collectedCoins.get(dolr)))+"");
        yourDolr.setImageDrawable(getResources().getDrawable(R.drawable.dolr));

        yourDolr.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView yourShil = mGroupListView.createItemView(shil);
        yourShil.setDetailText((coinHelper.sumCoinValue(spareChanges.get(shil)) + coinHelper.sumCoinValue(collectedCoins.get(shil)))+"");
        yourShil.setImageDrawable(getResources().getDrawable(R.drawable.shil));

        yourShil.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView yourQuid = mGroupListView.createItemView(quid);
        yourQuid.setDetailText((coinHelper.sumCoinValue(spareChanges.get(quid)) + coinHelper.sumCoinValue(collectedCoins.get(quid)))+"");
        yourQuid.setImageDrawable(getResources().getDrawable(R.drawable.quid));

        yourQuid.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {

                    QMUICommonListItemView viewList = (QMUICommonListItemView) v;
                    switch (viewList.getText().toString()) {
                        case "Gold Balance":
                            Log.d(tag,"balance selected");
                            break;

                        case "PENY":
                            System.out.println("Penny selected");
                            jumpToView(peny);
                            break;
                        case "DOLR":
                            System.out.println("Dolr selected");
                            jumpToView(dolr);
                            break;
                        case "SHIL":
                            System.out.println("Shil selected");
                            jumpToView(shil);
                            break;
                        case "QUID":
                            System.out.println("Quid selected");
                            jumpToView(quid);
                            break;

                    }
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    System.out.println("Select to view " + text);
                }
            }
        };

        QMUIGroupListView.newSection(this)
                .setTitle("Your Pocket")
                .addItemView(listItemName,onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(this)
                .setTitle("Your Coin")
                .addItemView(yourPeny,onClickListener)
                .addItemView(yourDolr,onClickListener)
                .addItemView(yourShil,onClickListener)
                .addItemView(yourQuid,onClickListener)
                .addTo(mGroupListView);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // If a coin is selected, jump to view a list of coin.
    public void jumpToView(String currency){

        ArrayList<Coin> collectCoin = collectedCoins.get(currency);
        ArrayList<Coin> spareChange = spareChanges.get(currency);
        Intent intent = new Intent();
        intent.setClass(this, DisplayCoinActivity.class);
        intent.putExtra("collectCoin", (ArrayList<Coin>) collectCoin);
        intent.putExtra("spareChange", (ArrayList<Coin>) spareChange);
        intent.putExtra("currency",currency);
        startActivityForResult(intent,0);
    }

}
