package com.example.ormancase4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    Button toMainActivityButton;
    Button sostavButton;
    Button toAddnInfoButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        toMainActivityButton = findViewById(R.id.toMainActivity);
        sostavButton = findViewById(R.id.sostavButton);
        sostavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Sostav.class);
                startActivity(intent);
            }
        });
        toAddnInfoButton = findViewById(R.id.toAddnInfo);
        toMainActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        toAddnInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AdditionalInfo.class);
                startActivity(intent);
            }
        });
    }
}
