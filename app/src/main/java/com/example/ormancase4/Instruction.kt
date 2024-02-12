package com.example.ormancase4

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Instruction : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InstructionItemAdapter
    private var instructionItemList: MutableList<InstructionItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.instruction)
        supportActionBar?.title = if (
            LanguageModel.getCurrentLanguage(this@Instruction) == "ru"
        ) "Инструкция" else "Нұсқаулық"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        recyclerView = findViewById(R.id.recyclerView)
        init()
    }

    private fun init() {
        instructionItemList.add(
            InstructionItem(
                R.drawable.advice1,
                "1",
                resources.getString(R.string.instruction1)
            )
        )
        instructionItemList.add(
            InstructionItem(
                R.drawable.advice2,
                "2",
                resources.getString(R.string.instruction2)
            )
        )
        instructionItemList.add(
            InstructionItem(
                R.drawable.advice3,
                "3",
                resources.getString(R.string.instruction3)
            )
        )
        instructionItemList.add(
            InstructionItem(
                R.drawable.advice4,
                "4",
                resources.getString(R.string.instruction4)
            )
        )
        adapter = InstructionItemAdapter(instructionItemList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}
