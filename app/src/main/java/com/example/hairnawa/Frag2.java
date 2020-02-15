package com.example.hairnawa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class Frag2 extends Fragment {
    private View view;
    private LineChart lineChart;
    private Button dailys, weeklys, monthlys, annuals;
    private TextView titleText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag2, container, false);
        final String id = getArguments().getString("id"); //아이디를 받아옴

        String month = "월간";

        titleText = view.findViewById(R.id.titleText);
        titleText.setText(getString(R.string.Monthlys));

        //lineChart = findViewById(R.id.chart);

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


        graphDrawing(entries, labels, month);

        dailys = view.findViewById(R.id.Dailys);
        weeklys = view.findViewById(R.id.Weeklys);
        monthlys = view.findViewById(R.id.Monthlys);
        annuals = view.findViewById(R.id.Annuals);

        dailys.setOnClickListener(myListener);
        weeklys.setOnClickListener(myListener);
        monthlys.setOnClickListener(myListener);
        annuals.setOnClickListener(myListener);

        dailys.setSelected(true);

        return view;
    }

    public void graphDrawing(List<Entry> entries, final ArrayList<String> labels, String label){
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
        xAxis.enableGridDashedLine(8,24,0);


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




//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                if (labels.size() > (int) value) {
//                    return labels.get((int) value);
//                } else return null;
//            }
//
//
//           // @Override
//            public int getDecimalDigits() {
//                return 0;
//            }
//
//     });


    }

/*
    public void btnColorChange(View view, R.id.getId()){

        Button myButton = findViewById(Dailys);

        myButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        //버튼이 클릭되었을 때 하고 싶은 일
                    }
                }
        );

        myButton.setOnTouchListener(
                new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                myButton.setBackgroundResource(R.drawable.mybutton_select);  //버튼이 선택되었을때 이미지를 교체
                                break;
                            case MotionEvent.ACTION_UP:
                                myButton.setBackgroundResource(R.drawable.mybutton_none_select); //버튼에서 손을 떼었을때 이미지를 복구
                                break;
                        }
                        return false;
                    }
                }
        );




        if(){
            btnV = findViewById(Dailys);
            btnV.setBackgroundColor(Color.parseColor("#9370db"));
            //btnV.setTextColor(getColor(Color.parseColor("#9370db")));
        }


        Button buttonEvent;
        buttonEvent = findViewById(R.id.);

    }
*/



    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TextView titleText = view.findViewById(R.id.titleText);
            dailys.setSelected(false);
            weeklys.setSelected(false);
            monthlys.setSelected(false);
            annuals.setSelected(false);

            if (view.getId() == R.id.Dailys) {
                dailys.setSelected(true);

                String day = "일간";
                titleText.setText(getString(R.string.Dailys));

                List<Entry> entries = new ArrayList<>();
                entries.add(new Entry(1, 2f));
                entries.add(new Entry(2, 2f));
                entries.add(new Entry(3, 3f));
                entries.add(new Entry(4, 4f));
                entries.add(new Entry(5, 6f));
                entries.add(new Entry(6, 9f));
                entries.add(new Entry(7, 3f));
                entries.add(new Entry(8, 4f));
                entries.add(new Entry(9, 5f));
                entries.add(new Entry(10, 7f));
                entries.add(new Entry(11, 10f));
                entries.add(new Entry(12, 8f));
                entries.add(new Entry(13, 5f));
                entries.add(new Entry(14, 6f));
                entries.add(new Entry(15, 8f));
                entries.add(new Entry(16, 7f));
                entries.add(new Entry(17, 9f));
                entries.add(new Entry(18, 1f));
                entries.add(new Entry(19, 1f));
                entries.add(new Entry(20, 2f));
                entries.add(new Entry(21, 0f));
                entries.add(new Entry(22, 0f));
                entries.add(new Entry(23, 0f));
                entries.add(new Entry(24, 3f));


                final ArrayList<String> labels = new ArrayList<>();
                labels.add("AM 1h");
                labels.add("AM 2h");
                labels.add("AM 3h");
                labels.add("AM 4h");
                labels.add("AM 5h");
                labels.add("AM 6h");
                labels.add("AM 7h");
                labels.add("AM 8h");
                labels.add("AM 9h");
                labels.add("AM 10h");
                labels.add("AM 11h");
                labels.add("AM 12h");
                labels.add("PM 1h");
                labels.add("PM 2h");
                labels.add("PM 3h");
                labels.add("PM 4h");
                labels.add("PM 5h");
                labels.add("PM 6h");
                labels.add("PM 7h");
                labels.add("PM 8h");
                labels.add("PM 9h");
                labels.add("PM 10h");
                labels.add("PM 11h");
                labels.add("PM 12h");


                graphDrawing(entries, labels, day);
            }

            if (view.getId() == R.id.Weeklys) {
                weeklys.setSelected(true);

                String week = "주간";
                titleText.setText(getString(R.string.Weeklys));

                List<Entry> entries = new ArrayList<>();
                entries.add(new Entry(1, 0f));
                entries.add(new Entry(2, 0f));
                entries.add(new Entry(3, 1f));
                entries.add(new Entry(4, 4f));
                entries.add(new Entry(5, 10f));
                entries.add(new Entry(6, 4f));
                entries.add(new Entry(7, 4f));


                final ArrayList<String> labels = new ArrayList<>();
                labels.add("Mon");
                labels.add("Tues");
                labels.add("Wed");
                labels.add("Thurs");
                labels.add("Fri");
                labels.add("Sat");
                labels.add("Sun");

                graphDrawing(entries, labels, week);
            }

            if (view.getId() == R.id.Monthlys) {
                monthlys.setSelected(true);


                String month = "월간";
                titleText.setText(getString(R.string.Monthlys));

                List<Entry> entries = new ArrayList<>();
                entries.add(new Entry(1, 0f));
                entries.add(new Entry(2, 0f));
                entries.add(new Entry(3, 1f));
                entries.add(new Entry(4, 4f));
                entries.add(new Entry(5, 10f));
                entries.add(new Entry(6, 4f));
                entries.add(new Entry(7, 4f));
                entries.add(new Entry(8, 5f));
                entries.add(new Entry(9, 8f));
                entries.add(new Entry(10, 7f));
                entries.add(new Entry(11, 10f));
                entries.add(new Entry(12, 8f));


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


                graphDrawing(entries, labels, month);
            }

            if (view.getId() == R.id.Annuals) {
                annuals.setSelected(true);

                String years = "연간";
                titleText.setText(getString(R.string.Annuals));

                List<Entry> entries = new ArrayList<>();
                entries.add(new Entry(1, 246f));
                entries.add(new Entry(2, 259f));
                entries.add(new Entry(3, 777f));
                entries.add(new Entry(4, 558f));
                entries.add(new Entry(5, 449f));


                final ArrayList<String> labels = new ArrayList<>();
                labels.add("2019");
                labels.add("2020");
                labels.add("2021");
                labels.add("2022");
                labels.add("2023");

                graphDrawing(entries, labels, years);

            }
        }
    };
}
