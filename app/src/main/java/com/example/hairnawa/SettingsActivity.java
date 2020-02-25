package com.example.hairnawa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button btn_logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() { //로그아웃 버튼을 누르면
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this) //다이얼로그를 띄움
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SaveSharedPreference.clearUserName(SettingsActivity.this); //자동 로그인 해제
                                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                                startActivity(intent); //SignActivity로 이동
                                finish(); //SettingsActivity 종료
                                MainActivity MA = MainActivity.mainActivity;
                                MA.finish(); //MainActivity 종료
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
            }
        });

    }
}
