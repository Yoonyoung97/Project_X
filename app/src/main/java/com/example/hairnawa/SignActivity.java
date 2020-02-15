package com.example.hairnawa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignActivity extends AppCompatActivity {

    private Button login, findId, findPassword, signUp; //아이디 찾기, 비밀번호 찾기는 나중에
    private EditText id, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_layout);

        DatabaseReference mUserReference  = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference userRef = mUserReference.child("Users");

        login = findViewById(R.id.login);
        findId = findViewById(R.id.findId);
        findPassword = findViewById(R.id.findPassword);
        signUp = findViewById(R.id.signUp);

        id = findViewById(R.id.loginId);
        password = findViewById(R.id.loginPassword);

        login.setOnClickListener(new View.OnClickListener() { //로그인 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(TextUtils.isEmpty(id.getText())) {
                            Toast.makeText(SignActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(password.getText())) {
                            Toast.makeText(SignActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if(dataSnapshot.child(id.getText().toString()).exists()) { //아이디가 데이터 베이스에 존재하고
                            User user = dataSnapshot.child(id.getText().toString()).getValue(User.class);
                            if(user.getPassword().equals(password.getText().toString())) { //비밀번호가 일치하면
                                Toast.makeText(SignActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent homeIntent;
                                if(user.getIsCEO()) { //사장님이로그인하면
                                    homeIntent = new Intent(getApplicationContext(), com.example.hairnawa.MainActivity.class);  //사장님 홈으로 이동
                                }
                                else {
                                    homeIntent = new Intent(getApplicationContext(), com.example.hairnawa.MainActivity_customer.class);  //고객님 홈으로 이동
                                }
                                homeIntent.putExtra("id", id.getText().toString()); //ID값을 전달함
                                startActivity(homeIntent);
                                finish();
                            }
                            else {
                                Toast.makeText(SignActivity.this, "아이디 및 비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(SignActivity.this, "아이디 및 비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() { //회원가입 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent); //액티비티 띄우기 //회원가입 페이지로 이동
            }
        });

    }
}
