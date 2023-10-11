package com.example.ormancase4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
public class NavigationActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        getSupportActionBar().setTitle("Навигация");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back_arrow);
    }
}
