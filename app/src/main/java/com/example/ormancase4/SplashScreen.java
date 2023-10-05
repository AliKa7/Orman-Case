package com.example.ormancase4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class SplashScreen extends AppCompatActivity {
    ImageView logoImage;
    TextView welcomeText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        logoImage = findViewById(R.id.logoImage);
        welcomeText = findViewById(R.id.welcome);
        logoImage.setAlpha(0.1f);
        welcomeText.setAlpha(0.1f);
        logoImage.animate().alpha(1f).setDuration(2000);
        welcomeText.animate().alpha(1f).setDuration(2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}
