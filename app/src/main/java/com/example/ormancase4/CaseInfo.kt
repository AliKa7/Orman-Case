package com.example.ormancase4

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CaseInfo : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var caseItemsList: MutableList<CaseItem> = ArrayList()
    private lateinit var adapter: CaseItemAdapter
    private lateinit var symptoms: Array<String>
    private lateinit var symptomAdapter: ArrayAdapter<String>
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adviceLabel: TextView
    private lateinit var videoView1: VideoView
    private lateinit var videoView2: VideoView
    private var isVideoOn: Boolean = false
    private lateinit var openDialogButton: Button

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.case_info)
        recyclerView = findViewById(R.id.recyclerView)
        init()
        supportActionBar?.title = if (
            LanguageModel.getCurrentLanguage(this@CaseInfo) == "ru"
        ) "Состав ящика" else "Кейс құрамы"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        val path1 = "android.resource://" + packageName + "/" + R.raw.video_about
        val path2 = "android.resource://" + packageName + "/" + R.raw.video_instruction
        videoView1 = findViewById(R.id.videoView1)
        videoView1.setVideoPath(path1)
        val mediaController1 = MediaController(this)
        mediaController1.setAnchorView(videoView1)
        mediaController1.cancelDragAndDrop()
        videoView1.setMediaController(mediaController1)
        videoView2 = findViewById(R.id.videoView2)
        videoView2.setVideoPath(path2)
        val mediaController2 = MediaController(this)
        mediaController2.setAnchorView(videoView2)
        videoView2.setMediaController(mediaController2)
        openDialogButton = findViewById(R.id.open_dialog_button)
        openDialogButton.setOnClickListener {
            if (!isVideoOn) { // никакое видео не играет
                showCustomDialog()
            } else { // видео играет
                if (videoView1.isPlaying) {
                    videoView1.stopPlayback()
                    videoView1.visibility = View.INVISIBLE
                }
                if (videoView2.isPlaying) {
                    videoView2.stopPlayback()
                    videoView2.visibility = View.INVISIBLE
                }
                openDialogButton.text = resources.getText(R.string.video_button)
                openDialogButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_color))
            }
            isVideoOn = !isVideoOn
        }
        //////////////
        adviceLabel = findViewById(R.id.adviceLabel)
        symptoms = arrayOf(
            resources.getString(R.string.issues_item1),
            resources.getString(R.string.issues_item2), resources.getString(R.string.issues_item3), resources.getString(R.string.issues_item4),
            resources.getString(R.string.issues_item5), resources.getString(R.string.issues_item6)
        )
        autoCompleteTextView = findViewById(R.id.autoCompleteText)
        symptomAdapter = ArrayAdapter(this, R.layout.symptom_item, symptoms)
        autoCompleteTextView.setAdapter(symptomAdapter)
        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position).toString()
            var advice = ""
            val layoutManager = recyclerView.layoutManager
            when (item) {
                resources.getString(R.string.issues_item1) -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 6)
                    advice = resources.getString(R.string.issues_solves1)
                }

                resources.getString(R.string.issues_item2) -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 3)
                    advice = resources.getString(R.string.issues_solves2)
                }

                resources.getString(R.string.issues_item3) -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 9)
                    advice = resources.getString(R.string.issues_solves3)
                }

                resources.getString(R.string.issues_item4) -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 8)
                    advice = resources.getString(R.string.issues_solves4)
                }

                resources.getString(R.string.issues_item5) -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 2)
                    advice = resources.getString(R.string.issues_solves5)
                }

                resources.getString(R.string.issues_item6) -> {
                    layoutManager!!.smoothScrollToPosition(recyclerView, RecyclerView.State(), 4)
                    advice = resources.getString(R.string.issues_solves6)
                }
            }
            adviceLabel.text = advice
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.ch_video_layout_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val buttonGeneralVideo: Button = dialog.findViewById(R.id.dialog_btn_general)
        val buttonWearingVideo: Button = dialog.findViewById(R.id.dialog_btn_wearing)
        buttonGeneralVideo.setOnClickListener {
            videoView1.visibility = View.VISIBLE
            videoView1.start()
            dialog.dismiss()
            openDialogButton.text = resources.getText(R.string.close_video)
            openDialogButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_color))
        }
        buttonWearingVideo.setOnClickListener {
            videoView2.visibility = View.VISIBLE
            videoView2.start()
            dialog.dismiss()
            openDialogButton.text = resources.getText(R.string.close_video)
            openDialogButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_color))
        }
        dialog.show()
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
                R.drawable.box_item_water_bottle,
                resources.getString(R.string.item2name),
                resources.getString(R.string.item2desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_acid,
                resources.getString(R.string.item3name),
                resources.getString(R.string.item3desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_bint,
                resources.getString(R.string.item4name),
                resources.getString(R.string.item4desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_nitroglycerine,
                resources.getString(R.string.item5name),
                resources.getString(R.string.item5desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_green_brilliant,
                resources.getString(R.string.item6name),
                resources.getString(R.string.item6desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_wisp,
                resources.getString(R.string.item7name),
                resources.getString(R.string.item7desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_peroxide,
                resources.getString(R.string.item8name),
                resources.getString(R.string.item8desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_plaster,
                resources.getString(R.string.item9name),
                resources.getString(R.string.item9desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_coal,
                resources.getString(R.string.item10name),
                resources.getString(R.string.item10desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_ammiak,
                resources.getString(R.string.item11name),
                resources.getString(R.string.item11desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_corvalol,
                resources.getString(R.string.item12name),
                resources.getString(R.string.item12desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_validol,
                resources.getString(R.string.item13name),
                resources.getString(R.string.item13desc)
            )
        )
        caseItemsList.add(
            CaseItem(
                R.drawable.box_item_vata,
                resources.getString(R.string.item14name),
                resources.getString(R.string.item14desc)
            )
        )
        adapter = CaseItemAdapter(caseItemsList)
        recyclerView.adapter = adapter
    }
}

