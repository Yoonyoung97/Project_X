package com.example.hairnawa;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity_customer extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //바텀 내비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private com.example.hairnawa.Frag1_customer frag1_customer;
    private com.example.hairnawa.Frag2_customer frag2_customer;
    private com.example.hairnawa.Frag3_customer frag3_customer;
    private com.example.hairnawa.Frag4_customer frag4_customer;
    private com.example.hairnawa.Frag5_customer frag5_customer;
    private ListView listView;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);

        Intent homeIntent = getIntent();
        id = homeIntent.getStringExtra("id"); //Intent에서 ID를 받아옴

        bottomNavigationView = findViewById(R.id.bottomNavi_customer);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.bottomNavi1_customer:
                        setFrag(1);
                        break;
                    case R.id.bottomNavi2_customer:
                        setFrag(2);
                        break;
                    case R.id.bottomNavi3_customer:
                        setFrag(3);
                        break;
                    case R.id.bottomNavi4_customer:
                        setFrag(4);
                        break;
                    case R.id.bottomNavi5_customer:
                        setFrag(5);
                        break;
                }
                return true;
            }
        });
        frag1_customer = new com.example.hairnawa.Frag1_customer();
        frag2_customer = new com.example.hairnawa.Frag2_customer();
        frag3_customer = new com.example.hairnawa.Frag3_customer();
        frag4_customer = new com.example.hairnawa.Frag4_customer();
        frag5_customer = new com.example.hairnawa.Frag5_customer();
        setFrag(1); //첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택

//        listView = findViewById(R.id.list); //프래그가서 쓰기
//        List<String> data = new ArrayList<>();
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
//        listView.setAdapter(adapter);
//        data.add("리나");
//        data.add("리나2");
//        data.add("리나3");
//        adapter.notifyDataSetChanged(); // 저장
    }

    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("id", id); // Key, Value //Fragment로 ID를 전달함

        switch(n) {
            case 1:
                frag1_customer.setArguments(bundle);
                ft.replace(R.id.main_frame_customer, frag1_customer);
                ft.commit();
                break;
            case 2:
                frag2_customer.setArguments(bundle);
                ft.replace(R.id.main_frame_customer, frag2_customer);
                ft.commit();
                break;
            case 3:
                frag3_customer.setArguments(bundle);
                ft.replace(R.id.main_frame_customer, frag3_customer);
                ft.commit();
                break;
            case 4:
                frag4_customer.setArguments(bundle);
                ft.replace(R.id.main_frame_customer, frag4_customer);
                ft.commit();
                break;
            case 5:
                frag5_customer.setArguments(bundle);
                ft.replace(R.id.main_frame_customer, frag5_customer);
                ft.commit();
                break;
        }

    }
}