package com.example.ormancase4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdditionalInfo extends AppCompatActivity {
    RecyclerView recyclerView;
    InstructionItemAdapter adapter;
    List<InstructionItem> instructionItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additional_info);
        getSupportActionBar().setTitle("Инструкция");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back_arrow);
        recyclerView = findViewById(R.id.recyclerView);
        init();
    }

    private void init() {
        instructionItemList.add(new InstructionItem(R.drawable.advice1, "1", "Опираясь на наше приложение, пройдите к местоположению ящика, которое указано на карте"));
        instructionItemList.add(new InstructionItem(R.drawable.advice2, "2", "Приблизившись к ящику, вы увидите табличку - ориентир"));
        instructionItemList.add(new InstructionItem(R.drawable.advice3, "3", "На поверхности вы обнаружите железную крышку с интегрированной ручкой. Откройте его"));
        instructionItemList.add(new InstructionItem(R.drawable.advice4, "4", "Откройте ящик и доставайте оттуда нужные вам вещи"));
        adapter = new InstructionItemAdapter(instructionItemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}
