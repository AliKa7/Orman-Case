package com.example.ormancase4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class ChooseRoleActivity extends AppCompatActivity {
    Button dude;
    Button forester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_role);
        dude = findViewById(R.id.button1);
        forester = findViewById(R.id.button2);
        dude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseRoleActivity.this, HomeActivity.class));
            }
        });
        forester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseRoleActivity.this, ForesterMainActivity.class));
            }
        });
    }
}
