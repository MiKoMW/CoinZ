package io.github.mikomw.coinz.ui.activity;

import android.arch.lifecycle.Lifecycle;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
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
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.user.User;
import io.github.mikomw.coinz.util.Date;
import io.github.mikomw.coinz.util.SerializableManager;
import io.github.mikomw.coinz.util.uploadUserData;


/**
 * The map activity is the main game activity, user are able to collect the coins in this activity.
 *
 * @author Songbo Hu
 */

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationEngineListener,
       PermissionsListener, NavigationView.OnNavigationItemSelectedListener{

    public static final String tag = "MapActivity";
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    ArrayList<Coin> todayCoins;
    HashMap<String,Marker> todayIcons;
    Location originLocation;

    HashSet<String> todayCollectedID;
    ArrayList<Coin> CollectedCoins;
    long lastUpdateTime;
    int lastCoinCollected;
    String Uid;
    User user;
    TextView UidTextView;
    TextView userNickName;
    ImageView avater;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the map view from mapbox.
        Mapbox.getInstance(this, getString(R.string.mapboxtoken));
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapbox);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Initialize the action bar and the drawer.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.account);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.menu));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Set onClick listener.
        navigationView.setNavigationItemSelectedListener(this);

        Uid = getIntent().getStringExtra("Uid");
        todayIcons = new HashMap<>();
        lastUpdateTime = System.currentTimeMillis();
        lastCoinCollected = 0;

        View headerView = navigationView.getHeaderView(0);
        userNickName = headerView.findViewById(R.id.drawer_NikeName);
        UidTextView = headerView.findViewById(R.id.drawer_email);

        // Enable our player to set up their user name.
        userNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("USerNameClick");
                final QMUIDialog.EditTextDialogBuilder builder =new QMUIDialog.EditTextDialogBuilder(MapActivity.this);
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
                            Toast.makeText(MapActivity.this, "Your nick name is: " + text, Toast.LENGTH_SHORT).show();
                            MapActivity.this.user.setNickName(text.toString());
                            userNickName.setText(user.getNickName());
                            SerializableManager.saveSerializable(MapActivity.this,user,"userInfo.data");
                            saveData();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(MapActivity.this, "Please enter your nick name.", Toast.LENGTH_SHORT).show();
                        }}}).show();

                }
        });

        // Onclick listener to copy the UID to the clip board.
        UidTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                Objects.requireNonNull(cm).setText(Uid);
                Toast.makeText(MapActivity.this, "Uid copied!", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wallet) {
            Log.d(tag,"Wallet Selected");
            saveData();
            Intent intent = new Intent(this, WalletActivity.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_guide) {

            Log.d(tag,"Guide Sellected");
            saveData();
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivityForResult(intent,0);

        } else if (id == R.id.nav_setting) {
            Log.d(tag,"setting selected;");
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

    public void onMapReady(MapboxMap mapboxMap) {

        if (mapboxMap == null) {
            Log.d(tag, "[onMapReady] mapBox is null");
        } else {
            map = mapboxMap;
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            Log.d(tag,"MapReady!");
            todayCollectedID = SerializableManager.readSerializable(this,"todayCollectedCoinID.data");
            todayCoins = SerializableManager.readSerializable(this,"todayCoins.coin");
            CollectedCoins = SerializableManager.readSerializable(this,"collectedCoin.data");
            user = SerializableManager.readSerializable(this,"userInfo.data");

            if(!user.getLastCollectedUpdateDate().equals(Date.getDateInfo().today)){
                todayCollectedID = new HashSet<>();
                saveData();
            }

            // Set up the initial coin marks for today's coin
            setIcon();
            enableLocation();
            initUserView();

        }

    };

    private void initUserView(){
        UidTextView.setText(user.getUID());
        userNickName.setText(user.getNickName());
    }


    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            Log.d(tag, "Permissions are granted");
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
            Log.d(tag, "Permissions are not granted");
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this)
                .obtainBestLocationEngineAvailable();
        locationEngine.setInterval(5000); // preferably every 5 seconds
        locationEngine.setFastestInterval(1000); // at most every second
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
        Log.d(tag,"Location engine initialized.");
    };

    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer() {

        if (mapView == null) {
            Log.d(tag, "mapView is null");
        } else {
            if (map == null) {
                Log.d(tag, "map is null");
            } else {

                locationLayerPlugin = new LocationLayerPlugin(mapView,
                        map, locationEngine);
                locationLayerPlugin.setLocationLayerEnabled(true);
                locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
                locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
                Lifecycle lifecycle = getLifecycle();
                lifecycle.addObserver(locationLayerPlugin);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //System.out.println(location.toString());
        if (location == null) {
            Log.d(tag, "[onLocationChanged] location is null");
        } else {
            Log.d(tag, "[onLocationChanged] location is not null");
            originLocation = location;
            setCameraPosition(location);
            collectCoins(location);
            updateIcon();
        }
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        Log.d(tag, "[onConnected] requesting location updates");
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain){
        Log.d(tag, "Permissions: " + permissionsToExplain.toString());
    }

    private void setCameraPosition(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onPermissionResult(boolean granted) {
        Log.d(tag, "[onPermissionResult] granted == " + granted);
        if (granted) {
            enableLocation();
        } else {
            Toast.makeText(MapActivity.this, "Location is necessary for this game.", Toast.LENGTH_SHORT).show();
            enableLocation();
        }
    }

    @Override
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
            return true;
        }

        if (id == R.id.action_market) {
            Intent intent = new Intent(this, MarketActivity.class);
            System.out.println("Market");
            saveData();
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_friend) {
            System.out.println("Friend");
            Intent intent = new Intent(this, FriendActivity.class);
            startActivity(intent);
            saveData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

        if(locationEngine != null){

            try {
                locationEngine.requestLocationUpdates();
            } catch(SecurityException ignored) {}
            locationEngine.addLocationEngineListener(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

        if(locationEngine != null){
            locationEngine.removeLocationEngineListener(this);
            locationEngine.removeLocationUpdates();
        }
    }
    private void setIcon(){
        IconFactory iconFactory = IconFactory.getInstance(this);
        Icon peny_marker;
        Icon quid_marker;
        Icon shil_marker;
        Icon dolr_marker;
        Icon defult_marker;
        Icon marker;

        peny_marker = iconFactory.fromResource(R.drawable.greencoin);
        quid_marker = iconFactory.fromResource(R.drawable.yellowcoin);
        shil_marker = iconFactory.fromResource(R.drawable.redcoin);
        dolr_marker = iconFactory.fromResource(R.drawable.bluecoin);
        defult_marker = iconFactory.fromResource(R.drawable.blackcoin);

        List<MarkerOptions> markerOptions = new ArrayList<>();
        for(Coin coin : todayCoins) {

            if(todayCollectedID.contains(coin.getId())){
                continue;
            }
            String currency = coin.getCurrency();

            switch (currency) {
                case ("PENY"):
                    marker = peny_marker;
                    break;
                case ("QUID"):
                    marker = quid_marker;
                    break;

                case ("SHIL"):
                    marker = shil_marker;
                    break;

                case ("DOLR"):
                    marker = dolr_marker;
                    break;
                default:
                    marker = defult_marker;
                    break;
            }
            //marker = iconFactory.fromResource(R.mipmap.google_logo);


            String value = " " + coin.getValue();
            MarkerOptions thisMark = new MarkerOptions().position(coin.getLatLng()).title(currency).snippet(value).icon(marker);
            markerOptions.add(thisMark);
            Marker markerTemp = map.addMarker(thisMark);
            todayIcons.put(coin.getId(),markerTemp);
            //map.addMarker(new MarkerOptions().position(coin.getLatLng()));
        }
        System.out.println(markerOptions.size());
        System.out.println("Set");
        updateIcon();

    }

    int con = 0;
    private void updateIcon(){

        for(String id : todayCollectedID) {
            if(!todayIcons.keySet().contains(id)){
                continue;
            }

            System.out.println("Remove " + id);
            System.out.println(++con);
            Marker thisMark = todayIcons.get(id);
            map.removeMarker(thisMark);
            todayIcons.remove(id);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * Check if there is any coin can be collected at current location.
     * @param location the current location
     */
    private void collectCoins(Location location){
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        for(Coin coin : this.todayCoins){
            if(coin.getLatLng().distanceTo(latLng) <= 25){
                if(!todayCollectedID.contains(coin.getId())){
                    todayCollectedID.add(coin.getId());
                    CollectedCoins.add(coin);

                    long thistime = System.currentTimeMillis();
                    int coinCollected = todayCollectedID.size();
                    if(thistime - lastUpdateTime >= 60000 || (coinCollected - lastCoinCollected) >= 5){
                        lastUpdateTime = thistime;
                        lastCoinCollected = coinCollected;
                        saveData();
                    }

                }
            }
        }

        // Coin collected.

    }

    // Save and upload the user's data.
    public void saveData(){
        SerializableManager.saveSerializable(this,todayCollectedID,"todayCollectedCoinID.data");
        SerializableManager.saveSerializable(this,CollectedCoins,"collectedCoin.data");
        uploadUserData uploadUserData = new uploadUserData(this);
        uploadUserData.execute(this.Uid);
        System.out.println(Uid);

    }
}
