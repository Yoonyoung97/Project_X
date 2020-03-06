package com.example.hairnawa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        if(SaveSharedPreference.getUserName(FirstAuthActivity.this).length() == 0) {
            // call Login Activity
            intent = new Intent(FirstAuthActivity.this, SignActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            // Call Next Activity
            id = SaveSharedPreference.getUserName(this).toString();
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("User")
                    .document(id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(document.getString("position").equals("1")){
                                        intent = new Intent(getApplicationContext(), com.example.hairnawa.MainActivity.class);
                                    } else {
                                        intent = new Intent(getApplicationContext(), com.example.hairnawa.MainActivity_customer.class);
                                    }
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                    finish();

                                }

                            }
                        }
                    });
        }
    }
}