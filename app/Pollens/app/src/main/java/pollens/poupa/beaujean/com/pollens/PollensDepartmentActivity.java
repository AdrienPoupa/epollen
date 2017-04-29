package pollens.poupa.beaujean.com.pollens;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollensDepartmentActivity extends AppCompatActivity implements
        OnChartValueSelectedListener  {

    protected HorizontalBarChart mChart;
    protected Cursor cursor;
    protected String department, code;
    protected HashMap<Integer, HashMap<String, Integer>> map;

    /**
     * Inner class to load data
     */
    private class LoadRisk extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;
        String number;
        PollensDepartmentActivity parent;

        public LoadRisk(String code, PollensDepartmentActivity pollensDepartmentActivity) {
            super();
            number = code;
            parent = pollensDepartmentActivity;
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

            // Load data from DB
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
            cursor = databaseHelper.getRisk(number);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();

            // Map <Name, Risk>
            map = new HashMap<Integer, HashMap<String, Integer>>();

            if(cursor != null) {
                int i = 0;
                while (cursor.moveToNext()) {
                    if (cursor.getInt(3) != 0) {
                        HashMap<String, Integer> nested = new HashMap<>();
                        nested.put(cursor.getString(1), cursor.getInt(3));
                        map.put(i, nested);
                        i++;
                    }
                }
                cursor.close();
            }

            TextView headerText = (TextView)findViewById(R.id.headerText);
            headerText.setText(department + " " + code);

            mChart = (HorizontalBarChart) findViewById(R.id.chart1);

            BarData data = new BarData();

            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            int dataCount=0;
            ArrayList<String> labels = new ArrayList<>();

            for (int i = 0; i < map.size(); i++) {
                HashMap<String, Integer> hmap = map.get(i);
                Map.Entry<String, Integer> mentry = hmap.entrySet().iterator().next();
                String key = mentry.getKey();
                float value = mentry.getValue();
                BarEntry entry = new BarEntry(dataCount,value);
                valueSet1.add(entry);
                labels.add(key);
                dataCount++;
            }

            List<IBarDataSet> dataSets = new ArrayList<>();
            BarDataSetColored bds1 = new BarDataSetColored(valueSet1, "pollens");
            int[] colorArray= { Color.GREEN, Color.YELLOW, Color.RED };
            bds1.setColors(colorArray);
            String[] xAxisLabels = labels.toArray(new String[0]);

            bds1.setStackLabels(xAxisLabels);
            dataSets.add(bds1);
            data.addDataSet(bds1);
            data.setDrawValues(true);
            data.setBarWidth(0.4f);

            XAxis xaxis = mChart.getXAxis();
            xaxis.setDrawGridLines(false);
            xaxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
            xaxis.setGranularityEnabled(true);
            xaxis.setGranularity(1);
            xaxis.setDrawLabels(true);
            xaxis.setCenterAxisLabels(true);
            xaxis.setLabelCount(dataCount+1);
            xaxis.setXOffset(140);
            xaxis.setDrawAxisLine(false);
            xaxis.setTextSize(12f);
            xaxis.setTextColor(Color.DKGRAY);

            /**
             * Custom class to assign the name of the tree
             */
            class CategoryBarChartXaxisFormatter implements IAxisValueFormatter {

                private String[] mValues;

                public CategoryBarChartXaxisFormatter(String[] values) {
                    this.mValues = values;
                }

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    // "value" represents the position of the label on the axis (x or y)
                    int val = (int) value;
                    String label;
                    if (val >= 0 && val < mValues.length) {
                        label = mValues[val];
                    }
                    else {
                        label = "";
                    }
                    return label;
                }

            }

            CategoryBarChartXaxisFormatter xaxisFormatter = new CategoryBarChartXaxisFormatter(xAxisLabels);
            xaxis.setValueFormatter(xaxisFormatter);

            YAxis yAxisLeft = mChart.getAxisLeft();
            yAxisLeft.setDrawGridLines(false);
            yAxisLeft.setDrawAxisLine(false);
            yAxisLeft.setEnabled(false);

            YAxis yAxisRight = mChart.getAxisRight();
            yAxisRight.setDrawGridLines(false);
            yAxisRight.setDrawAxisLine(false);
            yAxisRight.setEnabled(false);

            Legend legend = mChart.getLegend();
            legend.setEnabled(false);

            mChart.setFitBars(true);
            mChart.getLayoutParams().height=((dataCount+2)*100);
            mChart.setPadding(0,50,0,0);
            mChart.setData(data);
            mChart.setDescription(null);
            mChart.animateXY(1000, 1000);
            mChart.setBackgroundColor(Color.LTGRAY);
            mChart.invalidate();
        }
    }

    /**
     * Custom BarDataSet to set color depending on value
     */
    public class BarDataSetColored extends BarDataSet {
        public BarDataSetColored(List<BarEntry> yVals, String label) {
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
        department = intent.getStringExtra("department");
        code = intent.getStringExtra("code");

        // Get data
        new LoadRisk(code, this).execute();
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