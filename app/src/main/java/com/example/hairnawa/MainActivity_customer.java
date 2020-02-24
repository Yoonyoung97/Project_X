package com.example.hairnawa;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity_customer extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //바텀 내비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private com.example.hairnawa.Frag1_customer frag1_customer;
    private com.example.hairnawa.Frag2_customer frag2_customer;
    private com.example.hairnawa.Frag3_customer frag3_customer;
    private com.example.hairnawa.Frag4_customer frag4_customer;
    private com.example.hairnawa.Frag5_customer frag5_customer;
    private String id;
    private Toolbar toolbar_customer;
    private TabLayout tabs_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);

        Intent homeIntent = getIntent();
        id = homeIntent.getStringExtra("id"); //Intent에서 ID를 받아옴

        frag1_customer = new com.example.hairnawa.Frag1_customer();
        frag2_customer = new com.example.hairnawa.Frag2_customer();
        frag3_customer = new com.example.hairnawa.Frag3_customer();
        frag4_customer = new com.example.hairnawa.Frag4_customer();
        frag5_customer = new com.example.hairnawa.Frag5_customer();
        setFrag(1); //첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택

        toolbar_customer = findViewById(R.id.toolbar_customer);
        setSupportActionBar(toolbar_customer);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); //기존 타이틀을 보이지 않게 함

        tabs_customer = findViewById(R.id.tabs_customer);
        tabs_customer.addTab(tabs_customer.newTab().setText(R.string.bottomNavi1_customer));
        tabs_customer.addTab(tabs_customer.newTab().setText(R.string.bottomNavi2_customer));
        tabs_customer.addTab(tabs_customer.newTab().setText(R.string.bottomNavi3_customer));
        tabs_customer.addTab(tabs_customer.newTab().setText(R.string.bottomNavi4_customer));
        tabs_customer.addTab(tabs_customer.newTab().setText(R.string.bottomNavi5_customer));

        tabs_customer.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //선택된 탭 번호 반환
                int position = tab.getPosition();

                switch (position) {
                    case 0:
                        setFrag(1);
                        break;
                    case 1:
                        setFrag(2);
                        break;
                    case 2:
                        setFrag(3);
                        break;
                    case 3:
                        setFrag(4);
                        break;
                    case 4:
                        setFrag(5);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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