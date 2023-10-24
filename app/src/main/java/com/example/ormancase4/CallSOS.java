package com.example.ormancase4;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class CallSOS extends AppCompatActivity {
    ImageView imageView;
    Button sendSMSButton;
    int PERMISSION_CODE = 100;
    AnimatorSet animatorSet;
    boolean smsSended = false;
    RadioButton rbFireman;
    RadioButton rbForester;
    TextView phoneNumberLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_sos);
        getSupportActionBar().setTitle("Вызов SOS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back_arrow);
        phoneNumberLabel = findViewById(R.id.phoneNumberLabel);
        rbFireman = findViewById(R.id.radioButtonFireman);
        rbForester = findViewById(R.id.radioButtonForester);
        rbFireman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberLabel.setText("8 (754) 421-78-88");
            }
        });
        rbForester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberLabel.setText("8 (129) 758-55-44");
            }
        });
        imageView = findViewById(R.id.phoneImageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = getResources().getString(R.string.call_phone_number);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel: " + phoneNumber));
                startActivity(intent);
            }
        });
        sendSMSButton = findViewById(R.id.sendMessage);
        if (!checkPermission(Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_CODE);
        }
        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!smsSended) {
                    String phoneNumber = getResources().getString(R.string.send_message_phone_number);
                    String smsToSend = "Мои координаты: 50.450431, 30.521799";
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, smsToSend, null, null);
                    Snackbar.make(v, R.string.snackbar_text, Snackbar.LENGTH_LONG).show();
                    smsSended = true;
                    v.setAlpha(0.5f);
                }
                else {
                    Snackbar.make(v, "Сообщение уже отправлено!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        infiniteAnimation();
        if (ContextCompat.checkSelfPermission(CallSOS.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CallSOS.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        }
        if (ContextCompat.checkSelfPermission(CallSOS.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CallSOS.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_CODE);
        }
    }

    private void infiniteAnimation() {
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(imageView, "rotation", -10f, 10f);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(imageView, "scaleX", 0.9f, 1.1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(imageView, "scaleY", 0.9f, 1.1f);
        rotationAnim.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnim.setRepeatMode(ObjectAnimator.REVERSE);
        scaleXAnim.setRepeatCount(ObjectAnimator.INFINITE);
        scaleXAnim.setRepeatMode(ObjectAnimator.REVERSE);
        scaleYAnim.setRepeatCount(ObjectAnimator.INFINITE);
        scaleYAnim.setRepeatMode(ObjectAnimator.REVERSE);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotationAnim, scaleXAnim, scaleYAnim);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
