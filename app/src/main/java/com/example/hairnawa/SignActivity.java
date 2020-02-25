package com.example.hairnawa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignActivity extends AppCompatActivity {

    private Button login, findId, findPassword, signUp; //아이디 찾기, 비밀번호 찾기는 나중에
    private EditText id, password;
    private CheckBox cb_login;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_layout);

        //DatabaseReference mUserReference  = FirebaseDatabase.getInstance().getReference(); //실시간 데이터베이스
        //final DatabaseReference userRef = mUserReference.child("Users");

        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어

        login = findViewById(R.id.login);
        findId = findViewById(R.id.findId);
        findPassword = findViewById(R.id.findPassword);
        signUp = findViewById(R.id.signUp);
        id = findViewById(R.id.loginId);
        password = findViewById(R.id.loginPassword);
        cb_login = findViewById(R.id.cb_login);

        login.setOnClickListener(new View.OnClickListener() { //로그인 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { return; } //버튼 중복 클릭 방지
                mLastClickTime = SystemClock.elapsedRealtime();

                if(TextUtils.isEmpty(id.getText())) { //아이디 입력칸이 비어있으면
                    Toast.makeText(SignActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password.getText())) { //비밀번호 입력칸이 비어있으면
                    Toast.makeText(SignActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentReference docRef = db.collection("User").document(id.getText().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) { //아이디가 데이터 베이스에 존재하고
                                    if(document.getString("userPwd").equals(password.getText().toString())) { //비밀번호가 일치하면
                                        Intent homeIntent;
                                        if(document.getString("position").equals("1")) { //사장님이 로그인하면
                                            homeIntent = new Intent(getApplicationContext(), com.example.hairnawa.MainActivity.class);  //사장님 홈으로 이동
                                        }
                                        else {
                                            homeIntent = new Intent(getApplicationContext(), com.example.hairnawa.MainActivity_customer.class);  //고객님 홈으로 이동
                                        }
                                        if(cb_login.isChecked()) { //자동 로그인 CheckBox를 선택했다면
                                            SaveSharedPreference.setUserName(SignActivity.this, id.getText().toString()); //자동 로그인 설정
                                        }
                                        homeIntent.putExtra("id", id.getText().toString()); //ID값을 전달함
                                        startActivity(homeIntent);
                                        finish();
                                    }
                                    else { //아이디는 존재하지만 비밀번호가 일치하지 않으면
                                        Toast.makeText(SignActivity.this, "아이디 및 비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                } else { //아이디가 존재하지 않으면
                                    Toast.makeText(SignActivity.this, "아이디 및 비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } //public void onComplete
                    }); //docRef.get().addOnCompleteListener
                } //else
            } //public void onClick
        }); //login.setOnClickListener

        signUp.setOnClickListener(new View.OnClickListener() { //회원가입 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent); //액티비티 띄우기 //회원가입 페이지로 이동
            }
        });

        findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
