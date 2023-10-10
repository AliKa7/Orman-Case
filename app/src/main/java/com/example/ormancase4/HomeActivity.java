package com.example.ormancase4;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    Button toMainActivityButton;
    Button sostavButton;
    Button toAddnInfoButton;
    ImageButton imageButton;
    Button toCallSOSButton;
    Boolean tmp = true;
    AnimatorSet animatorSet;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //toMainActivityButton = findViewById(R.id.toMainActivity);
        sostavButton = findViewById(R.id.toCaseAppearanceButton);
        imageButton = findViewById(R.id.imageButton);
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(imageButton, "rotation", -10f, 10f);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(imageButton, "scaleX", 0.9f, 1.1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(imageButton, "scaleY", 0.9f, 1.1f);
        rotationAnim.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnim.setRepeatMode(ObjectAnimator.REVERSE);
        scaleXAnim.setRepeatCount(ObjectAnimator.INFINITE);
        scaleXAnim.setRepeatMode(ObjectAnimator.REVERSE);
        scaleYAnim.setRepeatCount(ObjectAnimator.INFINITE);
        scaleYAnim.setRepeatMode(ObjectAnimator.REVERSE);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotationAnim, scaleXAnim, scaleYAnim);
        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });
        toCallSOSButton = findViewById(R.id.toCallSOSButton);
        toCallSOSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CallSOS.class));
            }
        });



        sostavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Sostav.class);
                startActivity(intent);
            }
        });
        toAddnInfoButton = findViewById(R.id.toAddnInfoButton);
        /*toMainActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
        toAddnInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AdditionalInfo.class);
                startActivity(intent);
            }
        });

    }
}
