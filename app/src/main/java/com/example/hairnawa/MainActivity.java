package com.example.hairnawa;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //바텀 내비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private com.example.hairnawa.Frag1 frag1;
    private com.example.hairnawa.Frag2 frag2;
    private com.example.hairnawa.Frag3 frag3;
    private com.example.hairnawa.Frag4 frag4;
    private com.example.hairnawa.Frag5 frag5;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent homeIntent = getIntent();
        id = homeIntent.getStringExtra("id"); //Intent에서 ID를 받아옴

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.bottomNavi1:
                        setFrag(1);
                        break;
                    case R.id.bottomNavi2:
                        setFrag(2);
                        break;
                    case R.id.bottomNavi3:
                        setFrag(3);
                        break;
                    case R.id.bottomNavi4:
                        setFrag(4);
                        break;
                    case R.id.bottomNavi5:
                        setFrag(5);
                        break;
                }
                return true;
            }
        });
        frag1 = new com.example.hairnawa.Frag1();
        frag2 = new com.example.hairnawa.Frag2();
        frag3 = new com.example.hairnawa.Frag3();
        frag4 = new com.example.hairnawa.Frag4();
        frag5 = new com.example.hairnawa.Frag5();
        setFrag(1); //첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택
    }

    //프래그먼트 교체가 일어나는 실행문
    public void setFrag(int n) { //홈에서 setFrag 함수를 쓰기위해 public으로 함
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("id", id); // Key, Value //Fragment로 ID를 전달함

        switch(n) {
            case 1:
                frag1.setArguments(bundle);
                ft.replace(R.id.main_frame, frag1);
                ft.commit();
                break;
            case 2:
                frag2.setArguments(bundle);
                ft.replace(R.id.main_frame, frag2);
                ft.commit();
                break;
            case 3:
                frag3.setArguments(bundle);
                ft.replace(R.id.main_frame, frag3);
                ft.commit();
                break;
            case 4:
                frag4.setArguments(bundle);
                ft.replace(R.id.main_frame, frag4);
                ft.commit();
                break;
            case 5:
                frag5.setArguments(bundle);
                ft.replace(R.id.main_frame, frag5);
                ft.commit();
                break;
        }

    }
}
