package io.github.mikomw.coinz.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.coin.Coin;
import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.rateDBOperator;
import io.github.mikomw.coinz.user.User;
import io.github.mikomw.coinz.util.Date;
import io.github.mikomw.coinz.util.SerializableManager;
import io.github.mikomw.coinz.util.uploadUserData;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * The market fragment will show the exchange information and enable player to pay in a certain currency.
 *
 * @author Songbo Hu
 */
public class MarketFragment extends Fragment {

    public static final String tag = "MarketFragment";

    // the fragment initialization parameters
    // Param1 the corresponding currency of the page.
    private static final String ARG_PARAM1 = "param1";

    View root;

    private String thisCurrency;

    private OnFragmentInteractionListener mListener;

    public MarketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currency Parameter 1 the currency corresponding to this page.
     * @return A new instance of fragment MarketFragment.
     */
    public static MarketFragment newInstance(String currency) {
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, currency);
        fragment.setArguments(args);
        return fragment;
    }


    private LineChartView lineChart;
    private ListView listview;
    String[] date ;
    float [] score;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    User user;
    TextView havePaid;
    ArrayList<Coin> collectedCoin;
    ArrayList<Coin> spareChange;
    Coin current_coin;
    Boolean current_isCollected;
    TextView cur_coin_value_textview;
    TextView cur_coin_gold_textview;

    // Broadcast manager for synchronize between different fragment.
    LocalBroadcastManager broadcastManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is for null pointer safety. In normal scenario, it will not happen.
        if (getArguments() != null) {
            thisCurrency = getArguments().getString(ARG_PARAM1);
        }

        user = SerializableManager.readSerializable(getContext(),"userInfo.data");

        spareChange = SerializableManager.readSerializable(getContext(),"spareChange.data");
        collectedCoin = SerializableManager.readSerializable(getContext(),"collectedCoin.data");

        // Set the broadcasting receiver.
        registerReceiver();


    }

    // Initialize the view.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_market2, container, false);
        cur_coin_value_textview = root.findViewById(R.id.market_coinvalue);
        cur_coin_gold_textview = root.findViewById(R.id.market_coldvalue);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Read data from SQL database.
        rateDBOperator rateDBOperator = new rateDBOperator(getContext());


        // Query the exchange rate from the data base.
        ArrayList<String> months = Date.getDateInfo().month;

        date=new String[months.size()];
        score=new float[months.size()];
        int con = 0;

        for(String day : months){
            ExchangeRate exchangeRate = rateDBOperator.queryOne(day);
            date[con] = day;
            switch (thisCurrency){
                case "PENY":
                    score[con] = (float) exchangeRate.getPENY();
                    break;
                case "DOLR":
                    score[con] = (float) exchangeRate.getDOLR();
                    break;
                case "SHIL":
                    score[con] = (float) exchangeRate.getSHIL();
                    break;
                case "QUID":
                    score[con] = (float) exchangeRate.getQUID();
                    break;
                default:
                    score[con] = (float) exchangeRate.getPENY();
            }
            con++;
        }

        initView();
        getAxisXLables();
        getAxisPoints();
        initLineChart();

        // Set the text view.
        TextView currency = root.findViewById(R.id.market_fragment_curr);
        currency.setText(thisCurrency);
        TextView value = root.findViewById(R.id.market_fragment_value);
        ImageView icon = root.findViewById(R.id.market_fragment_curr_icon);
        value.setText("" + score[0] + " Gold");

        switch (thisCurrency) {
            case "PENY":
                icon.setImageDrawable(getResources().getDrawable(R.drawable.peny));

                break;
            case "DOLR":
                icon.setImageDrawable(getResources().getDrawable(R.drawable.dolr));
                break;
            case "SHIL":
                icon.setImageDrawable(getResources().getDrawable(R.drawable.shil));
                break;
            case "QUID":
                icon.setImageDrawable(getResources().getDrawable(R.drawable.quid));
                break;
            default:
                icon.setImageDrawable(getResources().getDrawable(R.drawable.peny));
        }

        // How many collected coins have been paid today.
        havePaid = root.findViewById(R.id.market_havepaied);


        Button select_coin = root.findViewById(R.id.market_selectcoin);
        select_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToView(thisCurrency);
            }
        });

        Button makedeal_button = root.findViewById(R.id.market_deal);
        makedeal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDeal();
            }
        });

        updatePage();
    }


    @Override
    public void onResume() {
        super.onResume();
        updatePage();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // unregister the listener and the receiver.
        mListener = null;
        broadcastManager.unregisterReceiver(mAdDownLoadReceiver);
    }


    public interface OnFragmentInteractionListener {
        // No use for our app.
        void onFragmentInteraction(Uri uri);
    }



    private void initLineChart(){

        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        // Initialize the axis.
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.parseColor("#D6D6D9")); // Set the text color.

        axisX.setTextSize(8);//Set text size.
        axisX.setMaxLabelChars(8);
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);
        axisX.setHasLines(true);


        Axis axisY = new Axis();
        axisY.setName("");
        axisY.setTextSize(8);
        data.setAxisYLeft(axisY);

        // Set the user action with the graph.
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.VERTICAL);
        lineChart.setMaxZoom((float) 3);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        lineChart.setHorizontalScrollBarEnabled(true);

        // Set how many data are we going to show on the x axis.
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

    private void initView() {
        listview=(ListView) root.findViewById(R.id.listview);
        lineChart = (LineChartView) root.findViewById(R.id.line_chart);
    }


    private void getAxisXLables(){

        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    private void getAxisPoints(){
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }

    private void updatePage(){

        if(!user.isUpdated(Date.getDateInfo().today)){
            System.out.println("User is not upadted!");
            user.resetSale();
            user.setLastPayUpdateDate(Date.getDateInfo().today);
        }

        // Discount the amount of coins for a user to pay in if the user payed a collected coin.
        havePaid.setText("You can pay in " +  (25 - user.getToday_sale()) + " more collected coins");
        if(current_coin!=null){
            cur_coin_value_textview.setText(current_coin.getValue() + "");
            cur_coin_gold_textview.setText((current_coin.getValue() * score[0]) + "");
        }else{
            cur_coin_value_textview.setText("Select a coin");
            cur_coin_gold_textview.setText("Select a coin");
        }



    }

    private void registerReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sale");
        broadcastManager.registerReceiver(mAdDownLoadReceiver, intentFilter);
    }


    private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String change = intent.getStringExtra("change");
            if ("yes".equals(change)) {

                // we can only update the UI in the main thread.
                new Handler().post(new Runnable() {
                    public void run() {
                        user = SerializableManager.readSerializable(getContext(),"userInfo.data");
                        updatePage();
                    }
                });
            }
        }
    };



    public void saveData(){

        SerializableManager.saveSerializable(getContext(),user,"userInfo.data");
        SerializableManager.saveSerializable(getContext(),collectedCoin,"collectedCoin.data");
        SerializableManager.saveSerializable(getContext(),spareChange,"spareChange.data");

        uploadUserData uploadUserData = new uploadUserData(getActivity());
        uploadUserData.execute(user.getUID());

    }

    // Jump tp the display coin activity in order to choose a coin to pay in.
    public void jumpToView(String currency){

        ArrayList<Coin> temp_collectCoin = new ArrayList<>();
        ArrayList<Coin> temp_spareChange = new ArrayList<>();
        Intent intent = new Intent();

        for(Coin coin:collectedCoin){
            if(coin.getCurrency().equals(currency)){
                temp_collectCoin.add(coin);
            }
        }
        for(Coin coin:spareChange){
            if(coin.getCurrency().equals(currency)){
                temp_spareChange.add(coin);
            }
        }

        intent.setClass(getContext(), displayCoinActivity.class);
        intent.putExtra("collectCoin", (ArrayList<Coin>) temp_collectCoin);
        intent.putExtra("spareChange", (ArrayList<Coin>) temp_spareChange);
        intent.putExtra("currency",currency);
        startActivityForResult(intent,0);

    }




    // Get the result from other activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(!data.hasExtra("result")){
            return;
        }
        current_coin = (Coin) data.getExtras().get("result");//得到新Activity 关闭后返回的数据
        current_isCollected = data.getExtras().getBoolean("collected");
        Log.d(tag,"the selected coin is collected: " + current_isCollected);
        Log.d(tag,"the selected coin is " + current_coin);
        updatePage();
    }

    public void makeDeal(){

        // No coin selected.
        if(current_coin == null){
            Toast.makeText(getContext(), "You need a select a coin to make the deal!",
                    Toast.LENGTH_SHORT).show();
            System.out.println("have not selected a coin!");
            return;
        }

        // If the user has paid 25 collected coin today.
        if(current_isCollected && user.getToday_sale() >= 25){
            System.out.println("IsCollected");
            Toast.makeText(getContext(), "You can only pay 25 collected coins a day!",
                    Toast.LENGTH_SHORT).show();

        }

        // If the coin is collected. we have to decrease the pay in allowance.
        if(current_isCollected) {
            collectedCoin.remove(current_coin);
            user.addBalance(current_coin.getValue() * score[0]);
            user.sale_coin();
            current_coin = null;
            updatePage();
            saveData();

            // Broad to all other fragment to update the UI.
            Intent intent = new Intent("sale");
            intent.putExtra("change", "yes");
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

            return;
        }

        if(!current_isCollected) {

            spareChange.remove(current_coin);
            user.addBalance(current_coin.getValue() * score[0]);
            current_coin = null;
            updatePage();
            saveData();
            return;
        }


    }
}