package com.example.hairnawa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class Frag3 extends Fragment {
    private View view;
    private CalendarView calendar;
    private TextView date, description;
    private ExpandableListView expandableListView;
    //ArrayList<ReservationData> reservationDataList;
    //ArrayList<ReservationGroup> reservationDataList;
    private static Vector<ParentData> reservationDataList = new Vector<>();
    private static int numberOfReservation = 0;
    private static String guestID, strDate, name, phone;

    final FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);
        final String id = getArguments().getString("id"); //아이디를 받아옴

        calendar = view.findViewById(R.id.calendarView);
        date = view.findViewById(R.id.date);
        description = view.findViewById(R.id.description);
        expandableListView = view.findViewById(R.id.listView);

        //this.InitializeData("2020.01.01");
        //ParentAdapter adapter = new ParentAdapter(getContext(), reservationDataList);
        //expandableListView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id){
//                Toast.makeText(getContext(), "click", Toast.LENGTH_LONG).show();
//            }
//        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String calendarDate = Integer.toString(year) + '.' +Integer.toString(month + 1) + '.' + dayOfMonth; //yyyy.MM.dd
                date.setText(calendarDate);
                InitializeData(calendarDate);
            }
        });
        return view;
    }

    public void InitializeData(String calendarDate)
    {
        reservationDataList = new Vector<>();
        numberOfReservation = 0;

//        reservationDataList.add(new ParentData("김리나", "202001311600", "네일", "010-9134-3736"));
//        reservationDataList.add(new ParentData("김민성", "202001311700", "커트", "010-1234-5678"));
//        reservationDataList.add(new ParentData("최세영", "202001311800", "펌", "010-1111-2222"));
//        reservationDataList.add(new ParentData("홍윤영", "202001311900", "클리닉", "010-3333-4444"));

        Date findDate = new Date(), findDate2 = new Date();
        try {
            findDate = new SimpleDateFormat("yyyy.MM.dd").parse("2020.02.12"); //calendarDate로 바꿔야함
            findDate2 = new SimpleDateFormat("yyyy.MM.dd").parse("2020.02.13"); //calendarDate + 1로 바꿔야함
        } catch (ParseException e) {
            Toast.makeText(getContext(), "날짜를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        CollectionReference colRef = db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf") //사장님 아이디로 바꿔야함
                .collection("shop")
                .document("5NrbvMcs1XzQVOenQaec")
                .collection("reservation");

        colRef.whereGreaterThanOrEqualTo("time", findDate)
                .whereLessThan("time", findDate2)
                .orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //reservationDataList = new Vector<>();
                    //numberOfReservation = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Date getDate = document.getDate("time");
                        strDate = new SimpleDateFormat("yyyyMMddHHmm").format(getDate);
                        guestID = document.getString("guestID");
                        db.collection("User")
                                .document(guestID)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                name = document.getString("name");
                                                phone = document.getString("phone-number");
                                                //reservationDataList.add(new ParentData(name, strDate, "염색", phone)); //서비스 바꿔야함
                                            }
                                            //reservationDataList.add(new ParentData(name, strDate, "염색", phone));
                                        } else {
                                            Toast.makeText(getContext(), "고객님의 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        reservationDataList.add(new ParentData(name, strDate, "염색", phone));
                        numberOfReservation++;
                    } //for (QueryDocumentSnapshot document : task.getResult())
                } else {

                }
                ParentAdapter adapter = new ParentAdapter(getContext(), reservationDataList);
                expandableListView.setAdapter(adapter);
                description.setText(numberOfReservation + "개의 예약이 있습니다.");
            }
        });



        /*Date findDate = new Date(), findDate2 = new Date();
        try {
            findDate = new SimpleDateFormat("yyyy.MM.dd").parse("2020.02.12"); //calendarDate로 바꿔야함
            findDate2 = new SimpleDateFormat("yyyy.MM.dd").parse("2020.02.13"); //calendarDate + 1로 바꿔야함
        } catch (ParseException e) {
            Toast.makeText(getContext(), "날짜를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        colRef.whereGreaterThanOrEqualTo("time", findDate)
                .whereLessThan("time", findDate2)
                .orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //String str = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //str += document.getId() + " => " + document.getData() + "\n";
                                //Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                                numberOfReservation = numberOfReservation + 1;
                                Date getDate = document.getDate("time");
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
                                strDate = (String) formatter.format(getDate);
                                //ArrayList<String> strSurgery = document.get("Surgery");
                                guestID = document.getString("guestID");
                                db.collection("User")
                                        .document(guestID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot doc = task.getResult();
                                                    if (doc.exists()) {
                                                        String name = doc.getString("name");
                                                        String date = strDate;
                                                        String phone = doc.getString("phone-number");
                                                        //reservationDataList.add(new ParentData(name, date, "네일", phone)); //guestID -> 이름, 서비스 바꿔야함
                                                    }
                                                } else {
                                                    Toast.makeText(getContext(), "고객님의 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(getContext(), "Error getting documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

         */
    } //public void InitializeData
}

