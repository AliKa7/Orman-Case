package com.example.ormancase4;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AfterFinding extends AppCompatActivity {
    Button button;
    EditText caseNumberET;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_finding);
        getSupportActionBar().setTitle("Ящик найден");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back_arrow);
        dbRef = FirebaseDatabase.getInstance().getReference("Cases");
        button = findViewById(R.id.sendDataButton);
        caseNumberET = findViewById(R.id.caseNumberET);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Данные отправлены! Спасибо за обратную связь!", Snackbar.LENGTH_LONG).show();
                String s = caseNumberET.getText().toString().trim();
                String caseKey = "Case " + s;
                DatabaseReference caseRef = dbRef.child(caseKey);
                caseRef.child("Fullness").setValue(2);
            }
        });
    }
}
