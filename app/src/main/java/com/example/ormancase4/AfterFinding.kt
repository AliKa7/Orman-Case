package com.example.ormancase4

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AfterFinding : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var caseNumberET: EditText
    private lateinit var dbRef: DatabaseReference
    private lateinit var checkBox1: CheckBox
    private lateinit var checkBox2: CheckBox
    private lateinit var checkBox3: CheckBox
    private lateinit var checkBox4: CheckBox
    private lateinit var checkBoxes: MutableList<CheckBox>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.after_finding)
        supportActionBar?.title = if (
            LanguageModel.getCurrentLanguage(this@AfterFinding) == "ru"
        ) "Ящик найден" else "Кейс табылған"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        checkBox1 = findViewById(R.id.checkbox1)
        checkBox2 = findViewById(R.id.checkBox2)
        checkBox3 = findViewById(R.id.checkbox3)
        checkBox4 = findViewById(R.id.checkbox4)
        checkBoxes = ArrayList()
        checkBoxes.add(checkBox1)
        checkBoxes.add(checkBox2)
        checkBoxes.add(checkBox3)
        checkBoxes.add(checkBox4)
        dbRef = FirebaseDatabase.getInstance().getReference("Cases")
        button = findViewById(R.id.sendDataButton)
        caseNumberET = findViewById(R.id.caseNumberET)
        button.setOnClickListener(View.OnClickListener { v ->
            val s = caseNumberET.getText().toString().trim { it <= ' ' }
            val caseKey = "Case $s"
            val caseRef = dbRef.child(caseKey)
            var checkedCount = 0
            for (checkBox in checkBoxes) {
                if (checkBox.isChecked) {
                    checkedCount++
                }
            }
            when (checkedCount) {
                0 -> {
                    Snackbar.make(v, resources.getString(R.string.snackbar_not_enough_data), Snackbar.LENGTH_LONG).show()
                    return@OnClickListener
                }

                1, 2, 3 -> {
                    caseRef.child("Fullness").setValue("Частично заполнен")
                }

                4 -> {
                    caseRef.child("Fullness").setValue("Пустой")
                }
            }
            Snackbar.make(v, resources.getString(R.string.snackbar_success), Snackbar.LENGTH_SHORT)
                .show()
        })
    }
}

