package com.example.hairnawa;

import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignUpActivity extends AppCompatActivity {
    private EditText id, password, password2, name, email, nickname, phoneNumber, businessNumber;
    private Button register, cancel, doubleCheck;
    private RadioButton ceo, customer;
    private RadioGroup isCEORadio;
    private LinearLayout businessLayout, resultLayout;
    private boolean idOK = false;
    private TextView doubleCheckResult;

    DatabaseReference mUserReference  = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = mUserReference.child("Users");
    //DatabaseReference Ref = mPostReference.child("password");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        id = findViewById(R.id.id_register);
        password = findViewById(R.id.password_register);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password2 = findViewById(R.id.password2);
        password2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        nickname = findViewById(R.id.nickname);
        phoneNumber = findViewById(R.id.phoneNumber);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        ceo = findViewById(R.id.ceo);
        customer = findViewById(R.id.customer);
        isCEORadio = findViewById(R.id.isCEO);
        isCEORadio.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        businessNumber = findViewById(R.id.businessNumber);
        register = findViewById(R.id.register);
        cancel = findViewById(R.id.cancel);
        businessLayout = findViewById(R.id.layout);
        businessLayout.setVisibility(View.GONE);
        doubleCheck = findViewById(R.id.doubleCheck);
        doubleCheckResult = findViewById(R.id.doubleCheckResult);
        resultLayout = findViewById(R.id.resultLayout);
        resultLayout.setVisibility(View.GONE);
    }

    //라디오 그룹 클릭 리스너
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup isCEO, @IdRes int i) {
            if(i == R.id.ceo){
                businessLayout.setVisibility(View.VISIBLE); //사장님을 선택했을 때 사업자번호를 입력하는 칸이 나오게 함
            }
            else if(i == R.id.customer){
                businessLayout.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String id = dataSnapshot.getValue(String.class);
                //Toast.makeText(SignActivity.this, id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() { //회원가입 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(id.getText().toString()).exists()) { //아이디가 데이터 베이스에 존재하고
                            Toast.makeText(SignUpActivity.this, "아이디가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                            idOK = false;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(TextUtils.isEmpty(id.getText())) {
                    Toast.makeText(SignUpActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!idOK) {
                    Toast.makeText(SignUpActivity.this, "아이디 중복확인을 눌러주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!password.getText().toString().equals(password2.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(name.getText())) {
                    Toast.makeText(SignUpActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(email.getText())) {
                    Toast.makeText(SignUpActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(nickname.getText())) {
                    Toast.makeText(SignUpActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!ceo.isChecked() && !customer.isChecked()) { //둘 다 고르지 않았다면
                    Toast.makeText(SignUpActivity.this, "직업을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(phoneNumber.getText())) {
                    Toast.makeText(SignUpActivity.this, "폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(ceo.isChecked() && TextUtils.isEmpty(businessNumber.getText())) {
                    Toast.makeText(SignUpActivity.this, "사업자 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else { //모두 입력 완료했다면
                    String idStr = id.getText().toString(),
                            passwordStr = password.getText().toString(),
                            nameStr = name.getText().toString(),
                            emailStr = email.getText().toString(),
                            nicknameStr = nickname.getText().toString(),
                            phoneNumberStr = phoneNumber.getText().toString(),
                            businessNumberStr = "0000000000"; //고객님
                    boolean isCEO = false; //고객님
                    if(ceo.isChecked()) { //사장님
                        isCEO = true;
                        businessNumberStr = businessNumber.getText().toString();
                    }
                    writeNewUser(idStr, nameStr, emailStr, passwordStr, nicknameStr, phoneNumberStr, isCEO, businessNumberStr);
                    Toast.makeText(SignUpActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() { //최소 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doubleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(id.getText().toString()).exists()) { //아이디가 데이터 베이스에 존재할 때
                            doubleCheckResult.setText("아이디가 이미 존재합니다.");
                            doubleCheckResult.setTextColor(Color.RED);
                            resultLayout.setVisibility(View.VISIBLE);
                            idOK = false;
                        }
                        else {
                            doubleCheckResult.setText("이 아이디는 사용 가능합니다.");
                            doubleCheckResult.setTextColor(Color.BLUE);
                            resultLayout.setVisibility(View.VISIBLE);

                            idOK = true;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void writeNewUser(String userId, String name, String email, String password,  String nickname, String phoneNumber, boolean isCEO, String businessNumber) {
        User user = new User(name, email, password, nickname, phoneNumber, isCEO, businessNumber);
        userRef.child(userId).setValue(user);
    }

    /*private void updateUserName(String userId, String newName) {
        mPostReference.child("Users").child(userId).child("username").setValue(newName);
    }*/

}
