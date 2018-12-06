package io.github.mikomw.coinz.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.android.core.permissions.PermissionsManager;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.user.Friends;
import io.github.mikomw.coinz.user.User;
import io.github.mikomw.coinz.util.SerializableManager;
import io.github.mikomw.coinz.util.uploadUserData;

public class FriendActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String tag = "FriendActivity";
    private PermissionsManager permissionsManager;

    String Uid;
    User user;

    TextView UidTextView;
    TextView userNickName;

    ImageView avater;

    DrawerLayout drawer;
    QMUIGroupListView mGroupListView;

    ArrayList<Coin> collectedCoin;
    ArrayList<Coin> spareChange;
    CollectionReference friendEnquiryPoll;
    CollectionReference exchangeEnquiryPoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

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
        UidTextView = headerView.findViewById(R.id.drawer_email);
        userNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("USerNameClick");
                final QMUIDialog.EditTextDialogBuilder builder =new QMUIDialog.EditTextDialogBuilder(FriendActivity.this);
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
                            Toast.makeText(FriendActivity.this, "Your nick name is: " + text, Toast.LENGTH_SHORT).show();
                            FriendActivity.this.user.setNickName(text.toString());
                            userNickName.setText(user.getNickName());
                            SerializableManager.saveSerializable(FriendActivity.this,user,"userInfo.data");
                            saveData();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(FriendActivity.this, "Please enter your nick name.", Toast.LENGTH_SHORT).show();
                        }}}).show();

            }
        });
        File newFile = new File(getFilesDir().getPath()+"/userInfo.data");
        System.out.println(newFile.exists());
        user = SerializableManager.readSerializable(this,"userInfo.data");

        spareChange = SerializableManager.readSerializable(this,"spareChange.data");
        collectedCoin = SerializableManager.readSerializable(this,"collectedCoin.data");
        this.Uid = user.getUID();
        initUserView();


        mGroupListView=findViewById(R.id.friends_list);


        QMUIGroupListView.Section newSection = QMUIGroupListView.newSection(this);
        newSection.setTitle("Friends");

        int con = 0;
        for(Friends friend : user.getFriendUIDList()){
            QMUICommonListItemView temp = mGroupListView.createItemView(friend.Nickname);
            temp.setId(con);
            temp.setDetailText(friend.UID);
            newSection.addItemView(temp,this);
            con++;
        }

        newSection.addTo(mGroupListView);

        Button add_friends_but = findViewById(R.id.add_frends);
        add_friends_but.setOnClickListener(this);

        Button syn_btn = findViewById(R.id.refresh);
        syn_btn.setOnClickListener(this);
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
            Intent intent = new Intent(this, MarketActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_friend) {
            System.out.println("Friend");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        friendEnquiryPoll = db.collection("friendsenquirypoll");
        exchangeEnquiryPoll = db.collection("exchangeenquirypoll");
        check_friend_enquiry();
        check_sale_enquiry();

        friendEnquiryPoll.
                whereEqualTo("to", Uid).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.out.println("Error");
                    return;
                }
                //check_friend_enquiry();
            }


        });

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



    @Override
    public void onClick(View v) {

        if(v instanceof QMUICommonListItemView)
        {
            QMUICommonListItemView temp_V = (QMUICommonListItemView) v;
            System.out.println(temp_V.getDetailText());
            popUpBottomMenu(temp_V.getDetailText().toString());
            return;
        }

        int id = v.getId();
        switch (id) {
            case R.id.add_frends:
                System.out.println("Add friendFs");
                final QMUIDialog.EditTextDialogBuilder builder =new QMUIDialog.EditTextDialogBuilder(FriendActivity.this);
                builder.setTitle("End friend enquiry!").setPlaceholder("Please enter your firend's UID.")
                        .setInputType(InputType.TYPE_CLASS_TEXT).addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }).addAction("Send!", new QMUIDialogAction.ActionListener() {

                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text =builder.getEditText().getText();
                        if (text !=null && text.length() >0)
                        {

                            Toast.makeText(FriendActivity.this, "Enquiry send to " + text, Toast.LENGTH_SHORT).show();


                            send_friend_enquiry(text.toString(),1);
                            dialog.dismiss();


                        }else {
                            Toast.makeText(FriendActivity.this, "Please enter your nick name.", Toast.LENGTH_SHORT).show();
                        }}}).show();
                break;

                case R.id.refresh:
                    check_friend_enquiry();
                    check_sale_enquiry();
                    break;
                }


        }

        private void send_friend_enquiry(String uid, long stage){
            Map<String, Object> enquiry = new HashMap<>();

            enquiry.put("from", this.user.getUID());
            enquiry.put("fromNickname",user.getNickName());
            enquiry.put("to", uid);
            enquiry.put("stage",stage);

            friendEnquiryPoll.document()
                    .set(enquiry);

        }

        private void check_friend_enquiry(){
                   friendEnquiryPoll.whereEqualTo("to", Uid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    long stage = (long) document.get("stage");
                                    String from = (String) document.get("from");
                                    String nickname = (String) document.get("fromNickname");

                                    if(stage == 1){
                                        new QMUIDialog.MessageDialogBuilder(FriendActivity.this)
                                                .setTitle("Friends Enquiry from " + nickname)
                                                .setMessage("Do you wanna add " + from + " as a friend?")
                                                .addAction("NO", new QMUIDialogAction.ActionListener() {
                                                    @Override
                                                    public void onClick(QMUIDialog dialog, int index) {
                                                        dialog.dismiss();
                                                        }
                                                })
                                                .addAction("YES", new QMUIDialogAction.ActionListener() {
                                                    @Override
                                                    public void onClick(QMUIDialog dialog, int index) {
                                                        dialog.dismiss();

                                                        user.addFriend(new Friends(from,nickname));
                                                        saveData();
                                                        document.getReference().delete();
                                                        send_friend_enquiry(from,2);
                                                        Toast.makeText(FriendActivity.this, "Friend added", Toast.LENGTH_SHORT).show();
                                                        updateListView(new Friends(from,nickname));

                                                    }
                                                })
                                                .show();
                                    } else{
                                        user.addFriend(new Friends(from,nickname));
                                        saveData();
                                        document.getReference().delete();
                                        updateListView(new Friends(from,nickname));

                                    }

                                }


                            } else {
                                System.out.println("Error getting documents.");
                            }
                        }
                    });
        }



    public void saveData(){

        SerializableManager.saveSerializable(this,user,"userInfo.data");
        SerializableManager.saveSerializable(this,collectedCoin,"collectedCoin.data");
        SerializableManager.saveSerializable(this,spareChange,"spareChange.data");

        uploadUserData uploadUserData = new uploadUserData(this);
        uploadUserData.execute(user.getUID());

    }


    public void updateListView(Friends friends){
        QMUIGroupListView.Section newSection = mGroupListView.getSection(0);
        QMUICommonListItemView temp = mGroupListView.createItemView(friends.Nickname);
        temp.setDetailText(friends.UID);
        newSection.addItemView( temp,this);
        System.out.println(newSection);
        mGroupListView.removeAllViews();
        newSection.addTo(mGroupListView);
    }

    public void popUpBottomMenu(String saleTo){
        final int sale_peny = 0;
        final int sale_shil = 1;
        final int sale_dolr = 2;
        final int sale_quid = 3;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(this);
        builder.addItem(R.drawable.peny, "Sale Peny", sale_peny, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.shil, "Sale Shil", sale_shil, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.dolr, "Sale Dolr", sale_dolr, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.quid, "Sale Quid", sale_quid, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case sale_peny:
                                Toast.makeText(FriendActivity.this, "Sale Peny", Toast.LENGTH_SHORT).show();
                                popSelectCoin(saleTo,"PENY");
                                break;
                            case sale_shil:
                                Toast.makeText(FriendActivity.this, "Sale Shil", Toast.LENGTH_SHORT).show();
                                popSelectCoin(saleTo,"SHIL");

                                break;
                            case sale_dolr:
                                Toast.makeText(FriendActivity.this, "Sale Dolr", Toast.LENGTH_SHORT).show();
                                popSelectCoin(saleTo,"DOLR");

                                break;
                            case sale_quid:
                                Toast.makeText(FriendActivity.this, "Sale Quid", Toast.LENGTH_SHORT).show();
                                popSelectCoin(saleTo,"QUID");

                                break;
                        }
                    }
                }).build().show();

    }

    public void popSelectCoin(String uid, String currency){

        ArrayList<Coin> temp_spare = new ArrayList<>();
        ArrayList<Coin> temp_collect = new ArrayList<>();

        for(Coin coin : collectedCoin){
            if(coin.getCurrency().equals(currency)){
                temp_collect.add(coin);
            }
        }


        for(Coin coin : spareChange){
            if(coin.getCurrency().equals(currency)){
                temp_spare.add(coin);
            }
        }

        final String[] items = new String[temp_collect.size() + temp_spare.size() + 2];

        int head_collected = 0;
        items[0] = "Collected Coins";
        int con = 1;
        for(Coin coin :temp_collect){
            items[con] = currency + coin.getValue();
            con++;
        }
        int head_spare = con;
        items[con] = "Spare Coins";
        con++;
        for(Coin coin :temp_spare){
            items[con] = currency + coin.getValue();
            con++;
        }


        final int checkedIndex = 1;
        new QMUIDialog.CheckableDialogBuilder(FriendActivity.this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == head_collected || which == head_spare){
                            return;
                        }
                        System.out.println(which);
                        Toast.makeText(FriendActivity.this, "You Select " + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        boolean isCollected = which<head_spare;
                        Coin thisCoin;
                        if(isCollected){
                            thisCoin = temp_collect.get(which - 1);
                        }else{
                            thisCoin = temp_spare.get(which - head_spare - 1);
                        }

                        popupConfirmOrder(uid,isCollected,thisCoin);
                    }
                })
                .show();

    }

    private void popupConfirmOrder(String uid, boolean isCollected, Coin coin){

        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(FriendActivity.this);
        builder.setTitle("Sale " + coin.getValue() + " " + coin.getCurrency() + " for:")
                .setPlaceholder("Set your order:")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Confirm", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0 ) {
                            Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");

                            if(pattern.matcher(text.toString()).matches()){
                                Toast.makeText(FriendActivity.this, "Order sent!", Toast.LENGTH_SHORT).show();

                                send_sale_enquiry(uid,coin,isCollected,Double.parseDouble(text.toString()), 1);

                                dialog.dismiss();
                            }else{
                                Toast.makeText(FriendActivity.this, "Please enter a valid price!", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(FriendActivity.this, "Please enter your offer.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();

    }


    private void send_sale_enquiry(String uid, Coin coin, boolean isCollected, double price ,long stage){
        Map<String, Object> enquiry = new HashMap<>();

        enquiry.put("from", this.user.getUID());
        enquiry.put("fromNickname",user.getNickName());
        enquiry.put("to", uid);
        enquiry.put("stage",stage);
        enquiry.put("coin_value",coin.getValue());
        enquiry.put("coin_currency",coin.getCurrency());
        enquiry.put("isCollected",isCollected);
        enquiry.put("price",price);
        exchangeEnquiryPoll.document()
                .set(enquiry);

    }


    private void check_sale_enquiry(){
        exchangeEnquiryPoll.whereEqualTo("to", Uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                long stage = (long) document.get("stage");
                                String from = (String) document.get("from");
                                String nickname = (String) document.get("fromNickname");
                                String coin_currency = (String) document.get("coin_currency");
                                double coin_value = (double) document.get("coin_value");
                                boolean is_collected = (boolean) document.get("isCollected");
                                double price = (double) document.get("price");

                                if(stage == 1){
                                    new QMUIDialog.MessageDialogBuilder(FriendActivity.this)
                                            .setTitle("Sale Enquiry from " + nickname)
                                            .setMessage("Do you wanna buy " + coin_value + " " + coin_currency + " for " + price+" gold?")
                                            .addAction("NO", new QMUIDialogAction.ActionListener() {
                                                @Override
                                                public void onClick(QMUIDialog dialog, int index) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .addAction("YES", new QMUIDialogAction.ActionListener() {
                                                @Override
                                                public void onClick(QMUIDialog dialog, int index) {
                                                    dialog.dismiss();
                                                    document.getReference().delete();
                                                    Coin tempCoin = new Coin("Spare",coin_value,coin_currency,new LatLng());
                                                    spareChange.add(tempCoin);
                                                    user.setBalance(user.getBalance() - price);
                                                    saveData();
                                                    send_sale_enquiry(from,tempCoin,is_collected,price, 2);
                                                    Toast.makeText(FriendActivity.this, "Offer Accepted!", Toast.LENGTH_SHORT).show();
                                                    updateListView(new Friends(from,nickname));

                                                }
                                            })
                                            .show();
                                } else{

                                    user.setBalance(user.getBalance() + price);

                                    if(is_collected){
                                        for(Coin coin : collectedCoin){
                                            if(coin.getValue() == coin_value && coin.getCurrency().equals(coin_currency)){
                                                collectedCoin.remove(coin);
                                                System.out.println("Found the coin!");
                                                break;

                                            }
                                        }
                                    }else{
                                        for(Coin coin : spareChange){
                                            if(coin.getValue() == coin_value && coin.getCurrency().equals(coin_currency)){
                                                spareChange.remove(coin);
                                                System.out.println("Found the coin!");
                                                break;

                                            }
                                        }
                                    }

                                    saveData();
                                    document.getReference().delete();

                                }

                            }


                        } else {
                            System.out.println("Error getting documents.");
                        }
                    }
                });
    }




}

