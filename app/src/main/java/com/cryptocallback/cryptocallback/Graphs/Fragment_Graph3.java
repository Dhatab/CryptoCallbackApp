package com.cryptocallback.cryptocallback.Graphs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.cryptocallback.cryptocallback.CoinDetailActivity.CoinDetailActivity;
import com.cryptocallback.cryptocallback.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by User on 4/12/2018.
 */

public class Fragment_Graph3 extends Fragment {

    public String url, open, time;
    private LineChart mChart;
    ArrayList<String> xValues = new ArrayList<String>();
    ArrayList<Entry> yVals1 = new ArrayList<Entry>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private String jsonResponse;
    private String coinDesc;
    private static final String  TAG = "Tab3Fragment";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph3_fragment, container, false);
        mChart = (LineChart) view.findViewById(R.id.chart3);
        mChart.setNoDataText("Getting Data From Server");
        mChart.setNoDataTextColor(Color.BLACK);
        CoinDetailActivity coinDetailActivity = new CoinDetailActivity();
        coinDesc = coinDetailActivity.coinSymbol;
        makeChart();

        return view;
    }

    private void makeChart() {

        StringRequest req = new StringRequest(Request.Method.GET, "https://min-api.cryptocompare.com/data/histohour?fsym=" + coinDesc + "&tsym=USD&limit=60&aggregate=12&e=CCCAGG",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());

                        try {
                            jsonResponse = "";
                            JSONObject jObject = new JSONObject(response);
                            JSONArray jsonArray = jObject.getJSONArray("Data");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject o = jsonArray.getJSONObject(i);

                                time = o.getString("time");
                                open = o.getString("close");

                                float val = Float.parseFloat(open);

                                yVals1.add(new Entry(i, val));

                                long unixSeconds = Long.parseLong(time);
                                Date date = new Date(unixSeconds * 1000L);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM");
                                sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                                String formattedDate = sdf.format(date);

                                xValues.add(formattedDate);
                                run(xValues,yVals1);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        Graph_AppController.getInstance().addToRequestQueue(req);
    }
        private void run(final ArrayList<String> xValues, ArrayList<Entry> Yvals1) {

                mChart.setBackgroundColor(Color.WHITE);
                mChart.setGridBackgroundColor(Color.WHITE);
                mChart.setDrawGridBackground(true);
                mChart.setDrawBorders(true);

                Legend l = mChart.getLegend();
                l.setEnabled(false);

                XAxis xAxis = mChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setAxisLineColor(Color.BLACK);
                xAxis.setTextColor(Color.BLACK);

                xAxis.setValueFormatter(new DefaultAxisValueFormatter(0) {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return xValues.get((int) value % xValues.size());
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                });

                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.setTextColor(Color.BLACK);
                leftAxis.setDrawAxisLine(true);
                leftAxis.setDrawZeroLine(false);
                leftAxis.setDrawGridLines(false);
                leftAxis.setGridColor(Color.WHITE);
                leftAxis.setAxisLineColor(Color.BLACK);

                mChart.getAxisRight().setEnabled(false);
                LineDataSet set1;


                set1 = new LineDataSet(yVals1, "DataSet 1");

                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(Color.rgb(114, 188, 212));
                set1.setDrawCircles(true);
                set1.setLineWidth(3f);
                set1.setCircleRadius(1f);
                set1.setCircleColor(Color.rgb(114, 188, 212));
                set1.setDrawFilled(true);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_graph_frag2);
                set1.setFillDrawable(drawable);

                dataSets.add(set1);

                LineData datab = new LineData(dataSets);
                datab.setDrawValues(false);

                mChart.setData(datab);
                mChart.setDrawMarkers(true);
                mChart.getDescription().setEnabled(false);
                IMarker marker = new Fragment_Graph3.YourMarkerView(getContext(),R.layout.custom_marker);
                mChart.setMarker(marker);

    }

    public class YourMarkerView extends MarkerView {

        private TextView tvContent;

        public YourMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);

            // find your layout components
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            tvContent.setText("$" + e.getY());

            // this will perform necessary layouting
            super.refreshContent(e, highlight);
        }

        private MPPointF mOffset;

        @Override
        public MPPointF getOffset() {

            if(mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
            }

            return mOffset;
        }
    }
}