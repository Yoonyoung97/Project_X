package com.example.hairnawa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


import static androidx.constraintlayout.widget.Constraints.TAG;

public class Frag5 extends Fragment {
    private View view;

    private ExpandableListView expandableListView;
    private static Vector<customerParent> customerParentsDataList = new Vector<>();
    private static int numberOfCustomer = 0;
    private static Map<String,Vector<customerData>> customer = new HashMap<String,Vector<customerData>>();
    final FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어
    private TextView description;
    public String Name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag5, container, false);
        description = view.findViewById(R.id.line);
        final String id = getArguments().getString("id"); //아이디를 받아옴
        expandableListView = view.findViewById(R.id.listView);
        totalInitializeData();
        Button Total = (Button) view.findViewById(R.id.Total);
        Total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalInitializeData();
            }
        });
        Button Newbe = (Button) view.findViewById(R.id.Newbe);
        Newbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newbeInitializeData();
            }
        });
        Button Old = (Button) view.findViewById(R.id.old);
        Old.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                oldInitializeData();
            }
        });
        return view;
    }




    public void totalInitializeData()
    {
        Name = new String();
        customerParentsDataList = new Vector<>();
        description.setText("예약정보를 불러오는 중입니다.");
        String currentTime= new SimpleDateFormat("yyyy.MM.dd")
                .format(new Date(System.currentTimeMillis()));


        CollectionReference colRef = db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf") //사장님 아이디로 바꿔야함
                .collection("shop")
                .document("5NrbvMcs1XzQVOenQaec")
                .collection("reservation");



        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (final QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String guestID = document.getString("guestID");
                    Name = new String();
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
                                        Name = (String)  documentSnapshot.getData().get("name");
                                    }
                                }
                            });

                    if(customer.containsKey(guestID)){
                        customer.get(guestID)
                                .add(new customerData(Name,
                                        new SimpleDateFormat("yyyyMMddHHmm").format(document.getDate("time")),
                                        document.getData().get("Surgery").toString(),
                                        Integer.parseInt(document.getData().get("price").toString())));
                    }else{
                        Vector<customerData> data = new Vector<customerData>();
                        data.add(new customerData(Name,
                                new SimpleDateFormat("yyyyMMddHHmm").format(document.getDate("time")),
                                document.getData().get("Surgery").toString(),
                                Integer.parseInt(document.getData().get("price").toString())));
                        customer.put(guestID, data);
                    }
                }
            }
        });
        customerParentsDataList = new Vector<>();
        for(String Key: customer.keySet()){

            String lastDate = customer.get(Key).get(customer.get(Key).size()-1).getDate();
            String lastService = customer.get(Key).get(customer.get(Key).size()-1).getService();
            String Name = customer.get(Key).get(customer.get(Key).size()-1).getUserName();
            Integer totalPrice = 0;
            for(customerData e: customer.get(Key)){
                totalPrice+= e.getPrice();
            }
            customerParentsDataList.add(new customerParent(Name,
                    totalPrice,
                    lastDate,
                    lastService,
                    customer.get(Key).size(),
                    Key));
            expandableListView.setAdapter(new Frag5Adapter(getContext(),customerParentsDataList));
        }
        expandableListView.setAdapter(new Frag5Adapter(getContext(),customerParentsDataList));
        numberOfCustomer = customerParentsDataList.size();
        description.setText("총 " + numberOfCustomer + " 명의 고마운 고객님들이 계십니다.");
        customer = new HashMap<String,Vector<customerData>>();
    }//end InitializeData();
    public void newbeInitializeData()
    {
        customerParentsDataList = new Vector<>();
        description.setText("예약정보를 불러오는 중입니다.");
        String currentTime= new SimpleDateFormat("yyyy.MM.dd")
                .format(new Date(System.currentTimeMillis()));


        CollectionReference colRef = db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf") //사장님 아이디로 바꿔야함
                .collection("shop")
                .document("5NrbvMcs1XzQVOenQaec")
                .collection("reservation");



        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (final QueryDocumentSnapshot document : queryDocumentSnapshots) {
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
                                        Name = (String) documentSnapshot.getData().get("name");
                                    }
                                }
                            });

                    if(customer.containsKey(guestID)){
                        customer.get(guestID)
                                .add(new customerData(Name,
                                        new SimpleDateFormat("yyyyMMddHHmm").format(document.getDate("time")),
                                        document.getData().get("Surgery").toString(),
                                        Integer.parseInt(document.getData().get("price").toString())));
                    }else{
                        Vector<customerData> data = new Vector<customerData>();
                        data.add(new customerData(Name,
                                new SimpleDateFormat("yyyyMMddHHmm").format(document.getDate("time")),
                                document.getData().get("Surgery").toString(),
                                Integer.parseInt(document.getData().get("price").toString())));
                        customer.put(guestID, data);
                    }
                }
            }
        });
        customerParentsDataList = new Vector<>();
        for(String Key: customer.keySet()){

            String lastDate = customer.get(Key).get(customer.get(Key).size()-1).getDate();
            String lastService = customer.get(Key).get(customer.get(Key).size()-1).getService();
            String Name = customer.get(Key).get(customer.get(Key).size()-1).getUserName();
            Integer totalPrice = 0;
            for(customerData e: customer.get(Key)){
                totalPrice+= e.getPrice();
            }
            if(customer.get(Key).size() == 1) {
                customerParentsDataList.add(new customerParent(Name,
                        totalPrice,
                        lastDate,
                        lastService,
                        customer.get(Key).size(),
                        Key));
            }
            expandableListView.setAdapter(new Frag5Adapter(getContext(),customerParentsDataList));
        }
        expandableListView.setAdapter(new Frag5Adapter(getContext(),customerParentsDataList));
        numberOfCustomer = customerParentsDataList.size();
        description.setText("총 " + numberOfCustomer + " 명의 고마운 고객님들이 계십니다.");
        customer = new HashMap<String,Vector<customerData>>();
    }//end InitializeData();
    public void oldInitializeData()
    {
        customerParentsDataList = new Vector<>();
        description.setText("예약정보를 불러오는 중입니다.");
        String currentTime= new SimpleDateFormat("yyyy.MM.dd")
                .format(new Date(System.currentTimeMillis()));


        CollectionReference colRef = db.collection("User")
                .document("2ttvmnVOCqVtiwyWPMzf") //사장님 아이디로 바꿔야함
                .collection("shop")
                .document("5NrbvMcs1XzQVOenQaec")
                .collection("reservation");



        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                final String[] name = new String[1];
                if (e != null) {
                    Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (final QueryDocumentSnapshot document : queryDocumentSnapshots) {
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
                                        Name = (String) documentSnapshot.getData().get("name");
                                    }
                                }
                            });

                    if(customer.containsKey(guestID)){
                        customer.get(guestID)
                                .add(new customerData(Name,
                                        new SimpleDateFormat("yyyyMMddHHmm").format(document.getDate("time")),
                                        document.getData().get("Surgery").toString(),
                                        Integer.parseInt(document.getData().get("price").toString())));
                    }else{
                        Vector<customerData> data = new Vector<customerData>();
                        data.add(new customerData(Name,
                                new SimpleDateFormat("yyyyMMddHHmm").format(document.getDate("time")),
                                document.getData().get("Surgery").toString(),
                                Integer.parseInt(document.getData().get("price").toString())));
                        customer.put(guestID, data);
                    }
                }
            }
        });
        customerParentsDataList = new Vector<>();
        for(String Key: customer.keySet()){

            String lastDate = customer.get(Key).get(customer.get(Key).size()-1).getDate();
            String lastService = customer.get(Key).get(customer.get(Key).size()-1).getService();
            String Name = customer.get(Key).get(customer.get(Key).size()-1).getUserName();
            Integer totalPrice = 0;
            for(customerData e: customer.get(Key)){
                totalPrice+= e.getPrice();
            }
            if(customer.get(Key).size() >=5) {
                customerParentsDataList.add(new customerParent(Name,
                        totalPrice,
                        lastDate,
                        lastService,
                        customer.get(Key).size(),
                        Key));
            }
            expandableListView.setAdapter(new Frag5Adapter(getContext(),customerParentsDataList));
        }
        expandableListView.setAdapter(new Frag5Adapter(getContext(),customerParentsDataList));
        numberOfCustomer = customerParentsDataList.size();
        description.setText("총 " + numberOfCustomer + " 명의 고마운 고객님들이 계십니다.");
        customer = new HashMap<String,Vector<customerData>>();
    }//end InitializeData();


    private boolean search(Vector<customerParent> e,String guestID){
        for(customerParent data : e){
            if(data.getGuestID() == guestID){
                return true;
            }
        }
        return false;
    };
    private int searchIndex(Vector<customerParent> e,String guestID) {
        int index = 0;
        for (customerParent data : e) {
            if (data.getGuestID() == guestID) {
                return index;
            }
            index++;
        }
        return 0;
    };
}
