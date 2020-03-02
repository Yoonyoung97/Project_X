package com.example.hairnawa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static android.graphics.Color.TRANSPARENT;

public class Frag4 extends Fragment {
    /**
     * ''' MyID : 사장님 ID '''
     * ''' strDate : 댓글 작성 시간 [Firebase Timestep] ---- String '''
     * ''' context : 댓글 내용 [Firebase String] ---- String '''
     * ''' score : 별점 [Firebase Number] ---- Integer '''
     * ''' commentDataList : comment를 화면에 뿌리기  위해 필요한 데이터 클래스 리스트'''
     * ''' numberOfCoummnet : comment 갯수 --- int'''
     **/
    private ListView ListView;
    private View view;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String MyID = "2ttvmnVOCqVtiwyWPMzf";
    private static Vector<CommentData> commentDataList = new Vector<>();
    private static Vector<CommentData> negativeData = new Vector<>();
    private static Vector<CommentData> positiveData = new Vector<>();
    private static int numberOfComment = 0;
    private static String score;
    private static String UserName;
    private static Vector<String> context = new Vector<>();
    private static Vector<graphData> graphDataVector = new Vector<>();
    private static List<BarEntry> graphDataList = new ArrayList<>();
    private BarChart BarChart;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag4, container, false);
        ListView = view.findViewById(R.id.comentListview);
        Button total = (Button) view.findViewById(R.id.total);
        final Button positive = (Button) view.findViewById(R.id.positive);
        Button negative = (Button) view.findViewById(R.id.negative);
        load_data();
        graphPositiveDataLoad();
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positive_data();


            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negative_data();
            }
        });
        return view;
    }

    public void load_data() {
        numberOfComment = 0;
        UserName = new String();
        context = new Vector<>();
        commentDataList = new Vector<>();

        CollectionReference colRef = db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf")
                .collection("Intro_shop")
                .document("cvc6Ui9sIa5goupShSgZ")
                .collection("comment");
        colRef.orderBy("Time")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                        if (e != null) {
                            Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (final QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("guestID") != null) {
                                String guestID = document.getString("guestID");
                                context.add(String.valueOf(document.getData().get("context")));
                                score = String.valueOf(document.getData().get("score"));

                                db.collection("User")
                                        .document(guestID)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                 @Override
                                                                 public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                                     if (e != null) {
                                                                         Toast.makeText(getContext(), "데이터를 부르지 못했습니다.", Toast.LENGTH_SHORT).show();
                                                                         return;
                                                                     }
                                                                     UserName = String.valueOf(documentSnapshot.getData().get("name"));
                                                                     commentDataList.add(new CommentData(
                                                                             UserName, //고객님 이름
                                                                             context.remove(0),
                                                                             score));
                                                                     if(context.isEmpty()){
                                                                         ListView.setAdapter(new CommentAdapter(getContext(), commentDataList));
                                                                     }
                                                                 }
                                                             }
                                        );
                            }
                            numberOfComment++;
                        }
                        ListView.setAdapter(new CommentAdapter(getContext(), commentDataList));

                    }
                });
    }
    public void positive_data(){
        positiveData = new Vector<>();
        numberOfComment = 0;
        context = new Vector<>();
        commentDataList = new Vector<>();
        CollectionReference colRef = db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf")
                .collection("Intro_shop")
                .document("cvc6Ui9sIa5goupShSgZ")
                .collection("comment");
        colRef.orderBy("Time")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                        if (e != null) {
                            Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (final QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("guestID") != null) {
                                String guestID = document.getString("guestID");
                                context.add(String.valueOf(document.getData().get("context")));
                                score = String.valueOf(document.getData().get("score"));

                                db.collection("User")
                                        .document(guestID)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                 @Override
                                                                 public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                                     if (e != null) {
                                                                         Toast.makeText(getContext(), "데이터를 부르지 못했습니다.", Toast.LENGTH_SHORT).show();
                                                                         return;
                                                                     }
                                                                     UserName = String.valueOf(documentSnapshot.getData().get("name"));
                                                                     if(Integer.parseInt(score)==1) {
                                                                         positiveData.add(new CommentData(
                                                                                 UserName, //고객님 이름
                                                                                 context.remove(0),
                                                                                 score));
                                                                     }
                                                                     if (context.isEmpty()) {
                                                                         ListView.setAdapter(new CommentAdapter(getContext(), positiveData));
                                                                     }
                                                                 }


                                                             }
                                        );

                            }
                            numberOfComment++;
                        }
                        for (CommentData data : commentDataList) {
                            if (Integer.parseInt(data.getScore().toString()) == 1) {
                                positiveData.add(data);
                            }
                        }
                        ListView.setAdapter(new CommentAdapter(getContext(), positiveData));

                    }
                });
    }
    public void negative_data(){
        negativeData = new Vector<>();
        numberOfComment = 0;
        context = new Vector<>();
        commentDataList = new Vector<>();
        CollectionReference colRef = db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf")
                .collection("Intro_shop")
                .document("cvc6Ui9sIa5goupShSgZ")
                .collection("comment");
        colRef.orderBy("Time")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                        if (e != null) {
                            Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (final QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("guestID") != null) {
                                String guestID = document.getString("guestID");
                                context.add(String.valueOf(document.getData().get("context")));
                                score = String.valueOf(document.getData().get("score"));

                                db.collection("User")
                                        .document(guestID)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                 @Override
                                                                 public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                                     if (e != null) {
                                                                         Toast.makeText(getContext(), "데이터를 부르지 못했습니다.", Toast.LENGTH_SHORT).show();
                                                                         return;
                                                                     }
                                                                     UserName = String.valueOf(documentSnapshot.getData().get("name"));
                                                                     if(Integer.parseInt(score) == 0) {
                                                                         negativeData.add(new CommentData(
                                                                                 UserName, //고객님 이름
                                                                                 context.remove(0),
                                                                                 score));
                                                                     }

                                                                     if (context.isEmpty()) {
                                                                         ListView.setAdapter(new CommentAdapter(getContext(), negativeData));
                                                                     }
                                                                 }


                                                             }
                                        );

                            }
                            numberOfComment++;
                        }
                        ListView.setAdapter(new CommentAdapter(getContext(), negativeData));

                    }
                });
    }

    public void graphPositiveDataLoad() {
        graphDataList= new ArrayList<BarEntry>();
        graphDataVector = new Vector<>();
        CollectionReference colRef = db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf")
                .collection("Intro_shop")
                .document("cvc6Ui9sIa5goupShSgZ")
                .collection("comment");

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() { //콜백 리스너
                                       @Override
                                       public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                           Integer positive[] = {0,0,0,0, 0,0,0,0, 0,0,0,0};
                                           Integer negative[] = {0,0,0,0, 0,0,0,0, 0,0,0,0};
                                           if (e != null) {
                                               Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                               return;
                                           }
                                           for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                               graphDataVector.add(
                                                       new graphData(
                                                               new SimpleDateFormat("yyyyMMdd").format(document.getDate("Time")),
                                                               String.valueOf(document.getData().get("score"))));
                                           }
                                           for (graphData data : graphDataVector) {
                                               Integer index = Integer.parseInt(data.getDate().substring(4, 6))-1;

                                               if (Integer.parseInt(data.getScore()) == 1) {
                                                   positive[index]++;
                                               }
                                               else if(Integer.parseInt(data.getScore()) == 0){
                                                   negative[index]++;
                                               }

                                           }

                                           for (graphData data : graphDataVector){
                                               int index = Integer.parseInt(data.getDate().substring(4, 6))-1;
                                               float list[] = {positive[index],negative[index]};
                                               graphDataList.add(new BarEntry(
                                                       Integer.parseInt(data.getDate().substring(4, 6)),
                                                       list));
                                           }

                                           Drawgraph(graphDataList,"리뷰");
                                       }
                                   }
        );
    }
    public void Drawgraph(List<BarEntry> graphData,String effect){
        BarChart = view.findViewById(R.id.chart);
        BarDataSet BarDataSet = new BarDataSet(graphData,effect);


        String label[] = {"Jan","feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        ArrayList<String> labels = new ArrayList<String>();
        for(String labelData : label){
            labels.add(labelData);
        }

        XAxis xAxis = BarChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(0.2f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setDrawLabels(true);

        YAxis yRAxis = BarChart.getAxisRight();
        yRAxis.setTextColor(Color.GRAY);
        yRAxis.setEnabled(false);


        YAxis yLAxis = BarChart.getAxisLeft();
        yLAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yLAxis.setAxisLineColor(Color.WHITE);
        yLAxis.setGranularity(1f);
        yLAxis.setAxisMinimum(1f);
        yLAxis.setAxisMaximum(10);
        yLAxis.setAxisMinimum(0);
        yLAxis.setLabelCount(11);


        Description description = new Description();
        description.setText("");
        BarData data = new BarData(BarDataSet);
        data.setBarWidth(0.4f);
        BarChart.setData(data);
        BarChart.setFitBars(true);
        BarDataSet.setColors(Color.parseColor("#9370db"));
        data.setValueTextColor(Color.parseColor("#9370db"));
        BarChart.setDoubleTapToZoomEnabled(false);
        BarChart.setDrawGridBackground(false);

        xAxis.setGranularityEnabled(true);
        BarChart.animateXY(2000, 100);
        BarChart.setDescription(description);
        BarChart.invalidate();

    }
}
