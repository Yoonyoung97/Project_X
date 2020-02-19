package com.example.hairnawa;

import android.os.Bundle;
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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
import java.util.Vector;

public class Frag3 extends Fragment {
    private View view;
    private CalendarView calendar;
    private TextView date, description;
    private ExpandableListView expandableListView;
    private static Vector<ParentData> reservationDataList = new Vector<>();
    private static int numberOfReservation = 0;
    private static Vector<String> strDate = new Vector<>(), price = new Vector<>();
    private static Vector<ArrayList<String>> surgery = new Vector<>();

    final FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);
        final String id = getArguments().getString("id"); //아이디를 받아옴

        calendar = view.findViewById(R.id.calendarView);
        date = view.findViewById(R.id.date);
        description = view.findViewById(R.id.description);
        expandableListView = view.findViewById(R.id.listView);

        Date currentTime = Calendar.getInstance().getTime();
        InitializeData(new SimpleDateFormat("yyyy.MM.dd").format(currentTime));

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //날짜를 선택할 때 마다 호출됨
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String calendarDate = Integer.toString(year) + '.' + Integer.toString(month + 1) + '.' + dayOfMonth; //yyyy.MM.dd
                InitializeData(calendarDate);
            }
        });

        return view;
    }

    public void InitializeData(String calendarDate)
    {
        date.setText(calendarDate);
        reservationDataList = new Vector<>();
        numberOfReservation = 0;
        strDate = new Vector<>();
        surgery = new Vector<>();
        price = new Vector<>();
        description.setText("예약정보를 불러오는 중입니다.");

        Date findDate = new Date(), findDate2 = new Date();
        try {
            findDate = new SimpleDateFormat("yyyy.MM.dd").parse(calendarDate); //선택한 날짜
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(findDate);
            calendar.add(Calendar.DATE, 1);
            findDate2 = new Date(calendar.getTimeInMillis()); //선택한 날짜 다음날
        } catch (ParseException e) {
            Toast.makeText(getContext(), "날짜를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        CollectionReference colRef = db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf") //사장님 아이디로 바꿔야함
                .collection("shop")
                .document("5NrbvMcs1XzQVOenQaec")
                .collection("reservation");

        colRef.whereGreaterThanOrEqualTo("time", findDate) //선택한 날부터
                .whereLessThan("time", findDate2) //다음날 전까지
                .orderBy("time") //시간 순서대로
                .addSnapshotListener(new EventListener<QuerySnapshot>() { //콜백 리스너
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("time") != null && document.get("guestID") != null) {
                                Date getDate = document.getDate("time");
                                strDate.add(new SimpleDateFormat("yyyyMMddHHmm").format(getDate));
                                surgery.add((ArrayList<String>) document.getData().get("Surgery"));
                                price.add(String.valueOf(document.getData().get("price")));
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
                                                    reservationDataList.add(new ParentData( //reservationDataList에 추가
                                                            documentSnapshot.getString("name"), //고객님 이름
                                                            strDate.remove(0), //예약 날짜
                                                            surgery.remove(0).toString(), //서비스
                                                            documentSnapshot.getString("phone-number"), //폰번호
                                                            price.remove(0))); //가격
                                                    if(strDate.isEmpty()) { //모든 예약을 reservationDataList에 추가했을 때
                                                        expandableListView.setAdapter(new ParentAdapter(getContext(), reservationDataList)); //없으면 실행 잘 안됨
                                                    }
                                                } else {
                                                    Toast.makeText(getContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } //if (document.get("time") != null && document.get("guestID") != null)
                            numberOfReservation++;
                        } //for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                        expandableListView.setAdapter(new ParentAdapter(getContext(), reservationDataList)); //데이터가 없을 때
                        description.setText(numberOfReservation + "개의 예약이 있습니다.");
                    } //public void onEvent
                }); //colRef. ... .addSnapshotListener(new EventListener<QuerySnapshot>()
    } //public void InitializeData
}

