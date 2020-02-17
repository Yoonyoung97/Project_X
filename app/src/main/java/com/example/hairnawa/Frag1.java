package com.example.hairnawa;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;


public class Frag1 extends Fragment {
    private View view;
    private Button myShop, sales, numberOfCustomer, averageSales, materialCost;
    private LineChart lineChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag1, container, false);
        final String id = getArguments().getString("id"); //아이디를 받아옴

        myShop = view.findViewById(R.id.myShop);
        sales = view.findViewById(R.id.sales);
        numberOfCustomer = view.findViewById(R.id.numberOfCustomer);
        averageSales = view.findViewById(R.id.averageSales);
        materialCost = view.findViewById(R.id.materialCost);

        lineChart = view.findViewById(R.id.chart);

        sales.setOnClickListener(myListener);
        numberOfCustomer.setOnClickListener(myListener);
        averageSales.setOnClickListener(myListener);
        materialCost.setOnClickListener(myListener);

        sales.setSelected(true);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1,0f));
        entries.add(new Entry(2,0f));
        entries.add(new Entry(3,1f));
        entries.add(new Entry(4,4f));
        entries.add(new Entry(5,10f));
        entries.add(new Entry(6,4f));
        entries.add(new Entry(7,4f));
        entries.add(new Entry(8,5f));
        entries.add(new Entry(9,8f));
        entries.add(new Entry(10,7f));
        entries.add(new Entry(11,10f));
        entries.add(new Entry(12,8f));


        final ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");

        graphDrawing(entries, labels, "매출");

        return view;
    }

    public void graphDrawing(List<Entry> entries, final ArrayList<String> labels, String label) {
        lineChart = view.findViewById(R.id.chart);

        LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#9370db"));
        lineDataSet.setCircleHoleColor(Color.WHITE);
        lineDataSet.setColor(Color.parseColor("#9370db"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.setTextColor(Color.parseColor("#9370db"));
        xAxis.enableGridDashedLine(8, 24, 0);


        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.GRAY);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        yLAxis.setEnabled(true);
        yRAxis.setAxisMinimum(0);


        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EaseInCubic);
        lineChart.invalidate();

        MyMarkerView marker = new MyMarkerView(getContext(), R.layout.markerviewtext);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);
        xAxis.setGranularityEnabled(true);
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TextView titleText = view.findViewById(R.id.titleText);
            sales.setSelected(false);
            numberOfCustomer.setSelected(false);
            averageSales.setSelected(false);
            materialCost.setSelected(false);

            switch (view.getId()) {
                case R.id.sales:

                    break;
                case R.id.numberOfCustomer:

                    break;
                case R.id.averageSales:

                    break;
                case R.id.materialCost:

                    break;
                default:
                    break;
            }
        }
    };
}
