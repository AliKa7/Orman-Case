package com.example.ormancase4

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AfterFinding : AppCompatActivity() {
    lateinit var button: Button
    lateinit var caseNumberET: EditText
    lateinit var dbRef: DatabaseReference
    lateinit var checkBox1: CheckBox
    lateinit var checkBox2: CheckBox
    lateinit var checkBox3: CheckBox
    lateinit var checkBox4: CheckBox
    lateinit var checkBoxes: MutableList<CheckBox>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.after_finding)
        supportActionBar!!.title = "Ящик найден"
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
            val caseRef = dbRef!!.child(caseKey)
            var checkedCount = 0
            for (checkBox in checkBoxes) {
                if (checkBox!!.isChecked) {
                    checkedCount++
                }
            }
            if (checkedCount == 0) {
                Snackbar.make(v, "Вы не заполнили все данные!", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            } else if (checkedCount == 4) {
                caseRef.child("Fullness").setValue("Пустой")
            } else if (checkedCount == 1 || checkedCount == 2 || checkedCount == 3) {
                caseRef.child("Fullness").setValue("Частично заполнен")
            }
            Snackbar.make(v, "Данные отправлены! Спасибо за обратную связь!", Snackbar.LENGTH_SHORT)
                .show()
        })
    }
}

