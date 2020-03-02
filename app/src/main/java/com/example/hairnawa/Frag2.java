package com.example.hairnawa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Frag2 extends Fragment {
    private View view;
    private LineChart lineChart;
    private Button dailys, weeklys, monthlys, annuals;
    private TextView titleText;
    private static List<Entry> entries = new ArrayList<>();
    private static ArrayList<String> labels = new ArrayList<>();
    private static ArrayList<Long> price = new ArrayList<>();

    final FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어
    final DocumentReference docRef = db.collection("User")
            .document("2ttvmnVOCqVtiwyWPMzf") //사장님 아이디로 바꿔야함
            .collection("shop")
            .document("5NrbvMcs1XzQVOenQaec");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag2, container, false);
        final String id = getArguments().getString("id"); //아이디를 받아옴

        String month = "월간";

        titleText = view.findViewById(R.id.titleText);
        titleText.setText(getString(R.string.Monthlys));

        //lineChart = findViewById(R.id.chart);

        entries = new ArrayList<>();
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


        labels = new ArrayList<>();
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

            if (view.getId() == R.id.Dailys) { //일간 현황 버튼을 클릭했을 때
                dailys.setSelected(true);
                titleText.setText(getString(R.string.Dailys));
                entries = new ArrayList<>();
                price = new ArrayList<>();
                for(int i = 0; i < 8; i++)
                    price.add(i, (long)0);

                Calendar mCalendar = Calendar.getInstance();
                final Date today = mCalendar.getTime(); //오늘
                mCalendar.add(Calendar.HOUR, -mCalendar.HOUR);
                mCalendar.add(Calendar.MINUTE, -mCalendar.MINUTE);
                mCalendar.add(Calendar.SECOND, -mCalendar.SECOND);
                final Date day0 = mCalendar.getTime(); //오늘 0시
                mCalendar.add(Calendar.DAY_OF_WEEK, -1);
                final Date day1 = mCalendar.getTime(); //1일 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -1);
                final Date day2 = mCalendar.getTime(); //2일 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -1);
                final Date day3 = mCalendar.getTime(); //3일 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -1);
                final Date day4 = mCalendar.getTime(); //4일 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -1);
                final Date day5 = mCalendar.getTime(); //5일 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -1);
                final Date day6 = mCalendar.getTime(); //6일 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -1);
                final Date day7 = mCalendar.getTime(); //7일 전

                docRef.collection("reservation")
                        .whereGreaterThanOrEqualTo("time", day7) //일주일 전부터
                        .whereLessThanOrEqualTo("time", today) //오늘까지
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    if (document.get("price") != null) {
                                        Date getDate = document.getDate("time");
                                        if (getDate.getTime() < day6.getTime())                                                 //일주일 전이면
                                            price.set(0, price.get(0) + (long)(document.getData().get("price")));
                                        else if (day6.getTime() <= getDate.getTime() && getDate.getTime() < day5.getTime())     //6일 전이면
                                            price.set(1, price.get(1) + (long)(document.getData().get("price")));
                                        else if (day5.getTime() <= getDate.getTime() && getDate.getTime() < day4.getTime())     //5일 전이면
                                            price.set(2, price.get(2) + (long)(document.getData().get("price")));
                                        else if (day4.getTime() <= getDate.getTime() && getDate.getTime() < day3.getTime())     //4일 전이면
                                            price.set(3, price.get(3) + (long)(document.getData().get("price")));
                                        else if (day3.getTime() <= getDate.getTime() && getDate.getTime() < day2.getTime())     //3일 전이면
                                            price.set(4, price.get(4) + (long)(document.getData().get("price")));
                                        else if (day2.getTime() <= getDate.getTime() && getDate.getTime() < day1.getTime())     //2일 전이면
                                            price.set(5, price.get(5) + (long)(document.getData().get("price")));
                                        else if (day1.getTime() <= getDate.getTime() && getDate.getTime() < day0.getTime())     //1일 전이면
                                            price.set(6, price.get(6) + (long)(document.getData().get("price")));
                                        else if (day0.getTime() <= getDate.getTime())                                           //오늘이면
                                            price.set(7, price.get(7) + (long)(document.getData().get("price")));
                                    }
                                }
                                for(int i = 0; i < 8; i++)
                                    entries.add(new Entry(i - 7, price.get(i)));
                                graphDrawing(entries, labels, "일간 ( 0 : 오늘 ), ( -1 : 하루 전 )");
                            }
                        });


            }

            if (view.getId() == R.id.Weeklys) { //주간 현황 버튼을 클릭했을 때
                weeklys.setSelected(true);
                titleText.setText(getString(R.string.Weeklys));
                entries = new ArrayList<>();
                price = new ArrayList<>();
                for(int i = 0; i < 6; i++)
                    price.add(i, (long)0);

                Calendar mCalendar = Calendar.getInstance();
                final Date today = mCalendar.getTime(); //오늘
                mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                final Date week0 = mCalendar.getTime(); //이번주 월요일
                mCalendar.add(Calendar.DAY_OF_WEEK, -7);
                final Date week1 = mCalendar.getTime(); //1주 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -7);
                final Date week2 = mCalendar.getTime(); //2주 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -7);
                final Date week3 = mCalendar.getTime(); //3주 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -7);
                final Date week4 = mCalendar.getTime(); //4주 전
                mCalendar.add(Calendar.DAY_OF_WEEK, -7);
                final Date week5 = mCalendar.getTime(); //5주 전

                docRef.collection("reservation")
                        .whereGreaterThan("time", week5) //5주 전부터
                        .whereLessThanOrEqualTo("time", today) //오늘까지
                        //.orderBy("time")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    if (document.get("price") != null) {
                                        Date getDate = document.getDate("time");
                                        if (getDate.getTime() < week4.getTime()) {                                                 //5주 전 ~ 4주 전이면
                                            price.set(0, price.get(0) + (long)(document.getData().get("price")));
                                        } else if (week4.getTime() <= getDate.getTime() && getDate.getTime() < week3.getTime()) {   //4주 전 ~ 3주 전이면
                                            price.set(1, price.get(1) + (long)(document.getData().get("price")));
                                        } else if (week3.getTime() <= getDate.getTime() && getDate.getTime() < week2.getTime()) {   //3주 전 ~ 2주 전이면
                                            price.set(2, price.get(2) + (long)(document.getData().get("price")));
                                        } else if (week2.getTime() <= getDate.getTime() && getDate.getTime() < week1.getTime()) {   //2주 전 ~ 1주 전이면
                                            price.set(3, price.get(3) + (long)(document.getData().get("price")));
                                        } else if (week1.getTime() <= getDate.getTime() && getDate.getTime() < week0.getTime()) {   //1주 전 ~ 이번주 월요일
                                            price.set(4, price.get(4) + (long)(document.getData().get("price")));
                                        } else if (week0.getTime() <= getDate.getTime()) {                                           //이번주 월요일 ~ 오늘
                                            price.set(5, price.get(5) + (long)(document.getData().get("price")));
                                        }
                                    }
                                }
                                for(int i = 0; i < 6; i++) {
                                    entries.add(new Entry(i - 5, price.get(i)));
                                }
                                graphDrawing(entries, labels, "주간 ( 0 : 이번 주 ), ( -1 : 1주 전 )");
                            }
                        });
            }

            if (view.getId() == R.id.Monthlys) {
                monthlys.setSelected(true);
                entries = new ArrayList<>();

                titleText.setText(getString(R.string.Monthlys));

                docRef.collection("record")
                        .orderBy("month")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    if (document.get("price") != null && document.get("month") != null) {
                                        entries.add(new Entry((long)document.getData().get("month"), (long)(document.getData().get("price"))));
                                    }
                                }
                                graphDrawing(entries, labels, "월간");
                            }
                        });
            }

            if (view.getId() == R.id.Annuals) {
                annuals.setSelected(true);

                String years = "연간";
                titleText.setText(getString(R.string.Annuals));

                entries = new ArrayList<>();
                entries.add(new Entry(1, 246f));
                entries.add(new Entry(2, 259f));
                entries.add(new Entry(3, 777f));
                entries.add(new Entry(4, 558f));
                entries.add(new Entry(5, 449f));


                labels = new ArrayList<>();
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
