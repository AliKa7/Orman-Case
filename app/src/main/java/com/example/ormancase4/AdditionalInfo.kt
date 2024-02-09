package com.example.ormancase4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AdditionalInfo : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: InstructionItemAdapter
    var instructionItemList: MutableList<InstructionItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.additional_info)
        supportActionBar!!.title = "Инструкция"
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
                "Опираясь на наше приложение, пройдите к местоположению ящика, которое указано на карте"
            )
        )
        instructionItemList.add(
            InstructionItem(
                R.drawable.advice2,
                "2",
                "Приблизившись к ящику, вы увидите табличку - ориентир"
            )
        )
        instructionItemList.add(
            InstructionItem(
                R.drawable.advice3,
                "3",
                "На поверхности вы обнаружите железную крышку с интегрированной ручкой. Откройте ее"
            )
        )
        instructionItemList.add(
            InstructionItem(
                R.drawable.advice4,
                "4",
                "Откройте ящик и доставайте оттуда нужные вам вещи. Внутри есть информационная брошюра, на коробке самоспасателя находится инструкция."
            )
        )
        adapter = InstructionItemAdapter(instructionItemList)
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}
