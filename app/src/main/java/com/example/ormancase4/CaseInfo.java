package com.example.ormancase4;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;
public class CaseInfo extends AppCompatActivity {
    RecyclerView recyclerView;
    List<CaseItem> caseItemsList = new ArrayList<>();
    CaseItemAdapter adapter;
    ImageView marlyaImg;
    Button marlyaImageButton;
    Button marlyaVideoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_info);
        getSupportActionBar().setTitle("Состав ящика");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back_arrow);
        recyclerView = findViewById(R.id.recyclerView);
        init();
        marlyaImageButton = findViewById(R.id.marlyaImageButton);
        marlyaImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFullScreenImage();
            }
        });
        marlyaVideoButton = findViewById(R.id.marlyaVideoButton);
        marlyaVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFullScreenVideo();
            }
        });
    }
    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        caseItemsList.add(new CaseItem(R.drawable.self_rescuirer, getResources().getString(R.string.item1name), getResources().getString(R.string.item1desc)));
        caseItemsList.add(new CaseItem(R.drawable.oxygen_baloon, getResources().getString(R.string.item2name), getResources().getString(R.string.item2desc)));
        caseItemsList.add(new CaseItem(R.drawable.analgin, getResources().getString(R.string.item3name), getResources().getString(R.string.item3desc)));
        caseItemsList.add(new CaseItem(R.drawable.bint, getResources().getString(R.string.item4name), getResources().getString(R.string.item4desc)));
        caseItemsList.add(new CaseItem(R.drawable.perocsid, getResources().getString(R.string.item5name), getResources().getString(R.string.item5desc)));
        caseItemsList.add(new CaseItem(R.drawable.water_bottle, getResources().getString(R.string.item6name), getResources().getString(R.string.item6desc)));
        caseItemsList.add(new CaseItem(R.drawable.scissors, getResources().getString(R.string.item7name), getResources().getString(R.string.item7desc)));
        adapter = new CaseItemAdapter(caseItemsList);
        recyclerView.setAdapter(adapter);
    }
    private void ShowFullScreenImage() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_full_screen_image);
        ImageView fullScreenImageView = dialog.findViewById(R.id.fullScreenImageView);
        fullScreenImageView.setImageResource(R.drawable.marlya);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fullScreenImageView.startAnimation(fadeIn);

        dialog.show();

        // Optional: Set an OnClickListener to dismiss the dialog with fade-out animation
        fullScreenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeOut = AnimationUtils.loadAnimation(CaseInfo.this, R.anim.fade_out);
                fullScreenImageView.startAnimation(fadeOut);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });
    }
    private void ShowFullScreenVideo() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_full_screen_video);
        VideoView fullScreenVideoView = dialog.findViewById(R.id.fullScreenVideoView);

        // Set the video file path or URI
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.howtomarlya;

        fullScreenVideoView.setVideoPath(videoPath);
        fullScreenVideoView.start();

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        dialog.findViewById(R.id.fullScreenVideoView).startAnimation(fadeIn);

        dialog.show();

        // Optional: Set an OnClickListener to dismiss the dialog with fade-out animation
        fullScreenVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeOut = AnimationUtils.loadAnimation(CaseInfo.this, R.anim.fade_out);
                dialog.findViewById(R.id.fullScreenVideoView).startAnimation(fadeOut);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });
    }
}
