package com.example.hairnawa;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;


public class Frag1 extends Fragment {
    private View view;
    private Button myShop, sales, numberOfCustomer, averageSales, materialCost, btn_reservationMore;
    private LineChart lineChart;
    private TextView tv_feedback;
    private ListView lv_reservation;
    private static ArrayList<String> reservationList;
    private static Vector<String> strTime = new Vector<>();
    private static Vector<ArrayList<String>> surgery = new Vector<>();
    private static ArrayAdapter<String> adapter;

    final FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어

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
        tv_feedback = view.findViewById(R.id.tv_feedback);
        btn_reservationMore = view.findViewById(R.id.btn_reservationMore);
        lv_reservation = view.findViewById(R.id.lv_reservation);

        reservationList = new ArrayList<>(); //오늘의 예약 손님 정보 보여줌
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, reservationList);
        lv_reservation.setAdapter(adapter);

        db.collection("User").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "데이터를 부르지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    tv_feedback.setText(name + " 사장님 반갑습니다."); // 피드백이 된다면,, 고치기,,,
                } else {
                    Toast.makeText(getContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Calendar mCalendar = Calendar.getInstance();
        Date today = mCalendar.getTime(); //오늘
        mCalendar.add(Calendar.DAY_OF_WEEK, 1);
        Date tomorrow = mCalendar.getTime(); //내일
        db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf") //사장님 아이디로 바꿔야함
                .collection("shop")
                .document("5NrbvMcs1XzQVOenQaec")
                .collection("reservation")
                .whereGreaterThanOrEqualTo("time", today) //오늘부터
                .whereLessThan("time", tomorrow) //내일 전까지
                .orderBy("time") //시간 순서대로
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("time") != null && document.get("guestID") != null) {
                                Date getDate = document.getDate("time");
                                strTime.add(new SimpleDateFormat("HH:mm").format(getDate));
                                surgery.add((ArrayList<String>) document.getData().get("Surgery"));
                                String guestID = document.getString("guestID");
                                db.collection("User")
                                        .document(guestID)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                if (e != null) {
                                                    Toast.makeText(getContext(), "데이터를 부르지 못했습니다.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                                    String reservation = //18:00 : 김리나 고객님 [커트, 염색]
                                                            strTime.remove(0) + " : "
                                                            + documentSnapshot.getString("name") + " 고객님 " //고객님 이름
                                                            + surgery.remove(0).toString(); //서비스
                                                    reservationList.add(reservation);
                                                    if(strTime.isEmpty()) { //모든 예약을 reservationList에 추가했을 때
                                                        adapter.notifyDataSetChanged(); // 저장
                                                    }
                                                } else {
                                                    Toast.makeText(getContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } //if (document.get("time") != null && document.get("guestID") != null)
                        } //for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                        adapter.notifyDataSetChanged(); // 저장
                    } //public void onEvent
                }); //db.collection("User"). ... .addSnapshotListener

        myShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //나의 샵(샵 소개 페이지?)으로 이동
            }
        });

        btn_reservationMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setFrag(3); //자세히보기 버튼을 누르면 예약현황으로 넘어감
            }
        });

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
