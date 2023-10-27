package com.example.ormancase4;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AfterFinding extends AppCompatActivity {
    Button button;
    EditText caseNumberET;
    DatabaseReference dbRef;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    List<CheckBox> checkBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_finding);
        getSupportActionBar().setTitle("Ящик найден");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back_arrow);
        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkbox3);
        checkBox4 = findViewById(R.id.checkbox4);
        checkBoxes = new ArrayList<>();
        checkBoxes.add(checkBox1);
        checkBoxes.add(checkBox2);
        checkBoxes.add(checkBox3);
        checkBoxes.add(checkBox4);
        dbRef = FirebaseDatabase.getInstance().getReference("Cases");
        button = findViewById(R.id.sendDataButton);
        caseNumberET = findViewById(R.id.caseNumberET);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = caseNumberET.getText().toString().trim();
                String caseKey = "Case " + s;
                DatabaseReference caseRef = dbRef.child(caseKey);
                int checkedCount = 0;
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isChecked()) {
                        checkedCount++;
                    }
                }
                if (checkedCount == 0) {
                    Snackbar.make(v, "Вы не заполнили все данные!", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (checkedCount == 4) {
                    caseRef.child("Fullness").setValue("Пустой");
                } else if (checkedCount == 1 || checkedCount == 2 || checkedCount == 3) {
                    caseRef.child("Fullness").setValue("Частично заполнен");
                }

                Snackbar.make(v, "Данные отправлены! Спасибо за обратную связь!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
