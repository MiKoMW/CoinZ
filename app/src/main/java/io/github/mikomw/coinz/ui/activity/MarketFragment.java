package io.github.mikomw.coinz.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.rateDBOperator;
import io.github.mikomw.coinz.util.Date;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MarketFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MarketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View root;
    // TODO: Rename and change types of parameters
    private String thisCurrency;

    private OnFragmentInteractionListener mListener;

    public MarketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MarketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarketFragment newInstance(String param1) {
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    private ListView listview;
    private LineChartView lineChart;

    String[] date ;
    float [] score;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            thisCurrency = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_market2, container, false);

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        rateDBOperator rateDBOperator = new rateDBOperator(getContext());

        ArrayList<String> months = Date.getDateInfo().month;

        //List<ExchangeRate> rates = rateDBOperator.queryMany();

        date=new String[months.size()];
        score=new float[months.size()];
        int con = 0;

        for(String day : months){
            ExchangeRate exchangeRate = rateDBOperator.queryOne(day);
            date[con] = exchangeRate.getDate();
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

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);    //折线图上每个数据点的形状，这里是圆形
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//设置字体颜色

        axisX.setTextSize(8);//设置字体大小
        axisX.setMaxLabelChars(8);//最多几个X轴坐标
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);
        axisX.setHasLines(true);


        Axis axisY = new Axis();
        axisY.setName("");
        axisY.setTextSize(8);
        data.setAxisYLeft(axisY);
        //设置行为属性，缩放、滑动、平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.VERTICAL);
        lineChart.setMaxZoom((float) 3);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        lineChart.setHorizontalScrollBarEnabled(true);
        //设置X轴数据的显示个数（x轴0-7个数据）
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
        System.out.println(date.length);
        System.out.println(mAxisXValues);

        for (int i = 0; i < date.length; i++) {
            System.out.println(date[i]);
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    private void getAxisPoints(){
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }



}




