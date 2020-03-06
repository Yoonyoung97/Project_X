package com.example.hairnawa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Frag4_customer extends Fragment { //임시 페이지
    private View view;
    private TextView tv_whereShop;
    private ImageButton ib_shop;

    final FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어
    final CollectionReference userRef = db.collection("User");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag4_customer, container, false);
        String id = getArguments().getString("id"); //아이디를 받아옴


        tv_whereShop = view.findViewById(R.id.tv_whereShop);
        CollectionReference reservationRef = userRef.document(id)
                                                    .collection("reservation");
        reservationRef.orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(queryDocumentSnapshots.isEmpty()) {
                    tv_whereShop.setText("최근에 방문한 뷰티샵이 없습니다.");
                    return;
                }
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    if (document.get("time") != null && document.get("ceo") != null) {
                        String CEO = document.getString("ceo");
                        userRef.document(CEO)
                                .collection("Intro_shop")
                                .document("cvc6Ui9sIa5goupShSgZ")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String shopName = document.getString("shop_name");
                                                tv_whereShop.setText(shopName);
                                            }
                                        }
                                    }
                                });
                    }
                }
            }
        });

        ib_shop = view.findViewById(R.id.ib_shop);
        ib_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myshopIntent = new Intent(getContext(), MyShopActivity.class); //임시
                startActivity(myshopIntent);
            }
        });

        return view;
    }
}
