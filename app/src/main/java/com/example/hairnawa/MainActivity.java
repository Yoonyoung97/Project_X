package com.example.hairnawa;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;
    private FragmentTransaction ft;
    private com.example.hairnawa.Frag1 frag1;
    private com.example.hairnawa.Frag2 frag2;
    private com.example.hairnawa.Frag3 frag3;
    private com.example.hairnawa.Frag4 frag4;
    private com.example.hairnawa.Frag5 frag5;
    private String id;
    private Toolbar toolbar;
    public TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent homeIntent = getIntent();
        id = homeIntent.getStringExtra("id"); //Intent에서 ID를 받아옴

        frag1 = new com.example.hairnawa.Frag1(); //홈
        frag2 = new com.example.hairnawa.Frag2(); //샵현황
        frag3 = new com.example.hairnawa.Frag3(); //예약현황
        frag4 = new com.example.hairnawa.Frag4(); //피드백&리뷰
        frag5 = new com.example.hairnawa.Frag5(); //고객관리
        setFrag(1); //첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택

        toolbar = findViewById(R.id.toolbar); //화면 상단에 actionbar 대신 toolbar 이용
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); //기존 타이틀을 보이지 않게 함

        tabs = findViewById(R.id.tabs); //tabs를 선택하면 해당 Fragment로 바뀌게 함
        tabs.addTab(tabs.newTab().setText(R.string.bottomNavi1));
        tabs.addTab(tabs.newTab().setText(R.string.bottomNavi2));
        tabs.addTab(tabs.newTab().setText(R.string.bottomNavi3));
        tabs.addTab(tabs.newTab().setText(R.string.bottomNavi4));
        tabs.addTab(tabs.newTab().setText(R.string.bottomNavi5));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    public void setFrag(int n) { //홈(frag1)에서 setFrag 함수를 쓰기위해 public으로 함
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //메뉴를 얻어옴 (오른쪽 상단에 있는 메뉴 버튼)
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu1: { // 나의 샵을 눌렀을 때
                Intent myshopIntent = new Intent(this, MyShopActivity.class);
                startActivity(myshopIntent);
                break;
            }
            case R.id.action_menu2: { //설정을 눌렀을 때
                Toast.makeText(this, "설정", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}