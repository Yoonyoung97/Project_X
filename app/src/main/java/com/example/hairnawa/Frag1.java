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
import com.google.firebase.firestore.CollectionReference;
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
    private Button sales, numberOfCustomer, averageSales, materialCost, btn_reservationMore;
    private LineChart lineChart;
    //private TextView tv_feedback;
    private ListView lv_reservation;
    private TextView tv_noReservation;
    private static ArrayList<String> reservationList;
    private static Vector<String> strTime = new Vector<>();
    private static Vector<ArrayList<String>> surgery = new Vector<>();
    private static ArrayAdapter<String> adapter;
    private static List<Entry> entries;
    private static ArrayList<String> labels; //레이블 왜 있는거지..?

    final FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어
    final CollectionReference recordRef = db.collection("User")
            .document("2ttvmnVOCqVtiwyWPMzf") //사장님 아이디로 바꿔야함
            .collection("shop")
            .document("5NrbvMcs1XzQVOenQaec")
            .collection("record");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag1, container, false);
        final String id = getArguments().getString("id"); //아이디를 받아옴

        sales = view.findViewById(R.id.sales);
        numberOfCustomer = view.findViewById(R.id.numberOfCustomer);
        averageSales = view.findViewById(R.id.averageSales);
        materialCost = view.findViewById(R.id.materialCost);
        lineChart = view.findViewById(R.id.chart);
        //tv_feedback = view.findViewById(R.id.tv_feedback);
        btn_reservationMore = view.findViewById(R.id.btn_reservationMore);
        lv_reservation = view.findViewById(R.id.lv_reservation);
        tv_noReservation = view.findViewById(R.id.tv_noReservation);

        reservationList = new ArrayList<>(); //오늘의 예약 손님 정보 보여줌
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, reservationList);
        lv_reservation.setAdapter(adapter);
        tv_noReservation.setVisibility(View.GONE);

        /*db.collection("User").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
        });*/

        Calendar mCalendar = Calendar.getInstance();
        Date today = mCalendar.getTime(); //오늘
        mCalendar.add(Calendar.DAY_OF_WEEK, 1);
        Date tomorrow = mCalendar.getTime(); //내일
        db.collection("User") //오늘의 예약 리스트를 얻어옴
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
                        } else if(queryDocumentSnapshots.isEmpty()) {
                            tv_noReservation.setVisibility(View.VISIBLE); //오늘 예약이 없으면 이 TextView를 보여줌
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
                        adapter.notifyDataSetChanged(); // reservationList 저장
                    } //public void onEvent
                }); //db.collection("User"). ... .addSnapshotListener

        btn_reservationMore.setOnClickListener(new View.OnClickListener() { //자세히보기 버튼을 누르면
            @Override
            public void onClick(View v) {
                //((MainActivity)getActivity()).setFrag(3);
                ((MainActivity)getActivity()).tabs.selectTab(((MainActivity)getActivity()).tabs.getTabAt(2), true); //예약현황으로 넘어감
            }
        });

        sales.setOnClickListener(myListener);
        numberOfCustomer.setOnClickListener(myListener);
        averageSales.setOnClickListener(myListener);
        materialCost.setOnClickListener(myListener);

        entries = new ArrayList<>();
        monthlySales();

        return view;
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TextView titleText = view.findViewById(R.id.titleText);
            sales.setSelected(false);
            sales.setBackgroundResource(R.drawable.nonclicked_border);
            numberOfCustomer.setSelected(false);
            numberOfCustomer.setBackgroundResource(R.drawable.nonclicked_border);
            averageSales.setSelected(false);
            averageSales.setBackgroundResource(R.drawable.nonclicked_border);
            materialCost.setSelected(false);
            materialCost.setBackgroundResource(R.drawable.nonclicked_border);

            entries = new ArrayList<>();
            labels = new ArrayList<>();

            switch (view.getId()) {
                case R.id.sales: //매출 버튼을 눌렀을 때
                    sales.setSelected(true);
                    sales.setBackgroundResource(R.drawable.clicked_border);
                    monthlySales();
                    break;
                case R.id.numberOfCustomer: //고객 수 버튼을 눌렀을 때
                    numberOfCustomer.setSelected(true);
                    numberOfCustomer.setBackgroundResource(R.drawable.clicked_border);
                    monthlyNumberOfCustomer();
                    break;
                case R.id.averageSales: //인당 평균 매출 버튼을 눌렀을 때
                    averageSales.setSelected(true);
                    averageSales.setBackgroundResource(R.drawable.clicked_border);
                    monthlyAverageSales();
                    break;
                case R.id.materialCost: //재료비 버튼을 눌렀을 때
                    materialCost.setSelected(true);
                    materialCost.setBackgroundResource(R.drawable.clicked_border);
                    monthlyMaterialCost();
                    break;
                default:
                    break;
            }
        }
    };

    public void monthlySales(){
        recordRef.orderBy("month")
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
                        graphDrawing(entries, labels, "매출");
                    }
                });
    }

    public void monthlyNumberOfCustomer() {
        recordRef.orderBy("month")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("customer") != null && document.get("month") != null) {
                                entries.add(new Entry((long)document.getData().get("month"), (long)(document.getData().get("customer"))));
                            }
                        }
                        graphDrawing(entries, labels, "고객수");
                    }
                });
    }

    public void monthlyAverageSales() {
        recordRef.orderBy("month")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("customer") != null && document.get("price") != null && document.get("month") != null) {
                                float averageSales = (long)(document.getData().get("price")) / (long)(document.getData().get("customer")); //인당 평균매출 = 매출 / 고객수
                                entries.add(new Entry((long)document.getData().get("month"), averageSales));
                            }
                        }
                        graphDrawing(entries, labels, "인당 평균매출");
                    }
                });
    }

    public void monthlyMaterialCost() {

    }

    public void graphDrawing(List<Entry> entries, final ArrayList<String> labels, String label) { //그래프 그리기

        lineChart = view.findViewById(R.id.chart);

        LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#7C4DFF"));
        lineDataSet.setCircleHoleColor(Color.WHITE);
        lineDataSet.setColor(Color.parseColor("#7C4DFF"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.setTextColor(Color.parseColor("#7C4DFF"));
        xAxis.enableGridDashedLine(8, 24, 0);


        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.parseColor("#7C4DFF"));

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
}
