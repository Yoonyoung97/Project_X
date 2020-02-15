package com.example.hairnawa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Vector;

public class Frag3 extends Fragment {
    private View view;
    private CalendarView calendar;
    private TextView textView;
    private ExpandableListView expandableListView;
    //ArrayList<ReservationData> reservationDataList;
    //ArrayList<ReservationGroup> reservationDataList;
    Vector<ParentData> reservationDataList = new Vector<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);
        final String id = getArguments().getString("id"); //아이디를 받아옴

        calendar = view.findViewById(R.id.calendarView);
        textView = view.findViewById(R.id.textView);
        expandableListView = view.findViewById(R.id.listView);

        this.InitializeData();

        ParentAdapter adapter = new ParentAdapter(getContext(), reservationDataList);

        expandableListView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id){
//                Toast.makeText(getContext(), "click", Toast.LENGTH_LONG).show();
//            }
//        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String day = Integer.toString(year) + '.' +Integer.toString(month + 1) + '.' + dayOfMonth;
                textView.setText(day);

            }
        });

        return view;
    }

    public void InitializeData()
    {
        reservationDataList = new Vector<>();

        reservationDataList.add(new ParentData("김리나", "202001311600", "네일", "01091343736"));
        reservationDataList.add(new ParentData("김민성", "202001311700", "커트", "01012345678"));
        reservationDataList.add(new ParentData("최세영", "202001311800", "펌", "01011112222"));
        reservationDataList.add(new ParentData("홍윤영", "202001311900", "클리닉", "01033334444"));

    }
}

