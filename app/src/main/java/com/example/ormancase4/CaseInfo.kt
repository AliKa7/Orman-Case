package com.example.ormancase4

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CaseInfo : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var caseItemsList: MutableList<CaseItem> = ArrayList()
    private lateinit var adapter: CaseItemAdapter
    private lateinit var symptoms: Array<String>
    private lateinit var buttonAbout: Button
    private lateinit var buttonInst: Button
    private lateinit var symptomAdapter: ArrayAdapter<String>
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adviceLabel: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.case_info)
        recyclerView = findViewById(R.id.recyclerView)
        init()
        supportActionBar!!.title = "Состав ящика"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        buttonAbout = findViewById(R.id.button_about)
        buttonInst = findViewById(R.id.button_inst)
        val path1 = "android.resource://" + packageName + "/" + R.raw.video_about
        val path2 = "android.resource://" + packageName + "/" + R.raw.video_instruction
        val videoView1 = findViewById<VideoView>(R.id.videoView1)
        videoView1.setVideoPath(path1)
        val mediaController1 = MediaController(this)
        mediaController1.setAnchorView(videoView1)
        videoView1.setMediaController(mediaController1)
        val videoView2 = findViewById<VideoView>(R.id.videoView2)
        videoView2.setVideoPath(path2)
        val mediaController2 = MediaController(this)
        mediaController2.setAnchorView(videoView2)
        videoView2.setMediaController(mediaController2)
        buttonAbout.setOnClickListener {
            if (videoView1.isPlaying) {
                buttonAbout.text = "Информация о самоспасателе"
                videoView1.stopPlayback()
                videoView1.visibility = View.INVISIBLE
            } else {
                buttonAbout.text = "Выключить видео"
                videoView1.visibility = View.VISIBLE
                videoView1.start()
            }
        }
        buttonInst.setOnClickListener {
            if (videoView2.isPlaying) {
                buttonInst.text = "Как надевать самоспасатель?"
                videoView2.stopPlayback()
                videoView2.visibility = View.INVISIBLE
            } else {
                buttonInst.text = "Выключить видео"
                videoView2.visibility = View.VISIBLE
                videoView2.start()
            }
        }
        adviceLabel = findViewById(R.id.adviceLabel)
        symptoms = arrayOf(
            "Сильное кровотечение",
            "Термический ожог", "Отравление угарным газом", "Дезинфицировать рану",
            "Тепловой удар", "Проблемы с сердцем"
        )
        autoCompleteTextView = findViewById(R.id.autoCompleteText)
        symptomAdapter = ArrayAdapter(this, R.layout.symptom_item, symptoms)
        autoCompleteTextView.setAdapter(symptomAdapter)
        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position).toString()
            var advice = ""
            val layoutManager = recyclerView.layoutManager
            when (item) {
                "Сильное кровотечение" -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 6)
                    advice = "Используйте жгут для перевязки раны"
                }

                "Термический ожог" -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 3)
                    advice = "Используйте марлевые бинты и перекись водорода"
                }

                "Отравление угарным газом" -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 9)
                    advice = "Используйте активированный уголь"
                }

                "Дезинфицировать рану" -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 8)
                    advice = "Используйте лейкопластырь или бриллиантовый зеленый"
                }

                "Тепловой удар" -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 2)
                    advice = "Используйте ацетилсалициловую кислоту"
                }

                "Проблемы с сердцем" -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 4)
                    advice = "Используйте нитроглицерин"
                }
            }
            adviceLabel.text = advice
        }
    }

    private fun init() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        caseItemsList.add(
            CaseItem(
                R.drawable.self_rescuirer,
                resources.getString(R.string.item1name),
                resources.getString(R.string.item1desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.water_bottle,
                resources.getString(R.string.item2name),
                resources.getString(R.string.item2desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.acid,
                resources.getString(R.string.item3name),
                resources.getString(R.string.item3desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.bint,
                resources.getString(R.string.item4name),
                resources.getString(R.string.item4desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.nitroglycerine,
                resources.getString(R.string.item5name),
                resources.getString(R.string.item5desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.green_brilliant,
                resources.getString(R.string.item6name),
                resources.getString(R.string.item6desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.wisp,
                resources.getString(R.string.item7name),
                resources.getString(R.string.item7desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.peroxide,
                resources.getString(R.string.item8name),
                resources.getString(R.string.item8desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.plaster,
                resources.getString(R.string.item9name),
                resources.getString(R.string.item9desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.coal,
                resources.getString(R.string.item10name),
                resources.getString(R.string.item10desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.ammiak,
                resources.getString(R.string.item11name),
                resources.getString(R.string.item11desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.corvalol,
                resources.getString(R.string.item12name),
                resources.getString(R.string.item12desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.validol,
                resources.getString(R.string.item13name),
                resources.getString(R.string.item13desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.vata,
                resources.getString(R.string.item14name),
                resources.getString(R.string.item14desc)
            )
        )
        adapter = CaseItemAdapter(caseItemsList)
        recyclerView.adapter = adapter
    }
}

