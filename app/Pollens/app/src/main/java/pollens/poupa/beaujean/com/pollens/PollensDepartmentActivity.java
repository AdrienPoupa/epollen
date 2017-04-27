package pollens.poupa.beaujean.com.pollens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollensDepartmentActivity extends AppCompatActivity implements
        OnChartValueSelectedListener  {

    protected HorizontalBarChart mChart;

    /**
     * Inner class to load data
     */
    private class LoadRisk extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;
        String number;

        public LoadRisk(String code) {
            super();
            number = code;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PollensDepartmentActivity.this);
            pd.setMessage("Chargement des risques");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            FeedDB feedDB = new FeedDB(getApplicationContext());
            feedDB.loadRisk(number);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
        }
    }

    /**
     * Custom BarDataSet to set color depending on value
     */
    public class MyBarDataSet extends BarDataSet {
        public MyBarDataSet(List<BarEntry> yVals, String label) {
            super(yVals, label);
        }

        @Override
        public int getColor(int index) {
            if(getEntryForIndex(index).getY() < 2) // less than 2 green
                return mColors.get(0);
            else if(getEntryForIndex(index).getY() < 4) // less than 4 orange
                return mColors.get(1);
            else // greater or equal than 4-5 red
                return mColors.get(2);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pollens_department);

        Intent intent = getIntent();
        String department = intent.getStringExtra("department");
        String code = intent.getStringExtra("code");

        // Get data
        new LoadRisk(code).execute();

        // Load data from DB
        // todo: move in thread
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        Cursor cursor = dbHelper.getRisk(code);

        // Map <Name, Risk>
        Map<String, Integer> map = new HashMap<>();

        if(cursor != null) {
            while (cursor.moveToNext()) {
                System.out.println("ligne: "+cursor.getString(0)+cursor.getString(1)+cursor.getString(2)+cursor.getString(3));
                map.put(cursor.getString(1), cursor.getInt(3));
            }
            cursor.close();
        }

        System.out.println(map);

        TextView headerText = (TextView)findViewById(R.id.headerText);
        headerText.setText(department + " " + code);

        mChart = (HorizontalBarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // 19 trees max
        mChart.setMaxVisibleValueCount(19);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        YAxis yl = mChart.getAxisLeft();
        //yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis yr = mChart.getAxisRight();
        //yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        setData(5);
        mChart.setFitBars(true);
        mChart.animateY(2500);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private void setData(int count) {

        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        // todo: hashmap (associative array) name => value
        // todo: display value for a name

        for (int i = 0; i < count; i++) {
            yVals1.add(new BarEntry(i * spaceForBar, i,
                    getResources().getDrawable(R.drawable.star)));
        }

        MyBarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (MyBarDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new MyBarDataSet(yVals1, "Pollens");

            int[] colorArray= { Color.GREEN, Color.YELLOW, Color.RED};
            set1.setColors(colorArray);

            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);

            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return "Test";
                }
            };

            mChart.getXAxis().setValueFormatter(formatter);

            mChart.setData(data);
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();
    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);

        MPPointF position = mChart.getPosition(e, mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency());

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {
    };
}