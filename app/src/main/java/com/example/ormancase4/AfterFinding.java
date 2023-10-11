package com.example.ormancase4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class AfterFinding extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_finding);
        getSupportActionBar().setTitle("Дальнейшие действия");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back_arrow);
    }
}
