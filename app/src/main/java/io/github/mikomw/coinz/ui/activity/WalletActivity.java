package io.github.mikomw.coinz.ui.activity;

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

public class WalletActivity extends AppCompatActivity {

    String tag = "WalletActivity";
    HashMap<String,ArrayList<Coin>> collectedCoins;
    HashMap<String,ArrayList<Coin>> spareChanges;

    QMUIGroupListView mGroupListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        collectedCoins = new HashMap<>();
        spareChanges = new HashMap<>();

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
            }
        });

        mGroupListView=findViewById(R.id.group_list_item_contact);

        QMUICommonListItemView listItemName = mGroupListView.createItemView("Gold Balance");
        listItemName.setDetailText("GoldBalance");
        listItemName.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);



        QMUICommonListItemView yourPeny = mGroupListView.createItemView("Peny");
        yourPeny.setDetailText("GoldBalance");
        yourPeny.setImageDrawable(getResources().getDrawable(R.drawable.peny));

        yourPeny.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView yourDolr = mGroupListView.createItemView("Dolr");
        yourDolr.setDetailText("GoldBalance");
        yourDolr.setImageDrawable(getResources().getDrawable(R.drawable.dolr));

        yourDolr.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView yourShil = mGroupListView.createItemView("Shil");
        yourShil.setDetailText("GoldBalance");
        yourShil.setImageDrawable(getResources().getDrawable(R.drawable.shil));

        yourShil.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView yourQuid = mGroupListView.createItemView("Quid");
        yourQuid.setDetailText("GoldBalance");
        yourQuid.setImageDrawable(getResources().getDrawable(R.drawable.quid));

        yourQuid.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {

                    QMUICommonListItemView viewList = (QMUICommonListItemView) v;
                    switch (viewList.getText().toString()) {
                        case "Gold Balance":
                            System.out.println("想要balance");
                            break;
                        case "Peny":
                            System.out.println("想要Peny");
                            break;
                        case "Dolr":
                            System.out.println("想要Dolr");
                            break;
                        case "Shil":
                            System.out.println("想要Shil");
                            break;
                        case "Quid":
                            System.out.println("想要Quid");
                            break;

                    }
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    System.out.println(text);
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
}
