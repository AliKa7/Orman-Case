package com.example.ormancase4

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class CallSOS : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var sendSMSButton: Button
    private lateinit var rbFireman: RadioButton
    private lateinit var rbForester: RadioButton
    private lateinit var phoneNumberLabel: TextView
    private var PERMISSION_CODE = 100
    private lateinit var animatorSet: AnimatorSet
    private var smsSent = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.call_sos)
        supportActionBar?.title = if (
            LanguageModel.getCurrentLanguage(this@CallSOS) == "ru"
        ) "Вызов SOS" else "SOS шақыру"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        phoneNumberLabel = findViewById(R.id.phoneNumberLabel)
        rbFireman = findViewById(R.id.radioButtonFireman)
        rbForester = findViewById(R.id.radioButtonForester)
        rbFireman.setOnClickListener {
            phoneNumberLabel.text = "8 (771) 111-78-68"
        }
        rbForester.setOnClickListener {
            phoneNumberLabel.text = "8 (129) 758-55-44"
        }
        imageView = findViewById(R.id.phoneImageView)
        imageView.setOnClickListener {
            val phoneNumber = resources.getString(R.string.call_phone_number)
            val intent = Intent(Intent.ACTION_CALL)
            intent.setData(Uri.parse("tel: $phoneNumber"))
            startActivity(intent)
        }
        sendSMSButton = findViewById(R.id.sendMessage)
        if (!checkPermission(Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                PERMISSION_CODE
            )
        }
        sendSMSButton.setOnClickListener { v ->
            if (!smsSent) {
                val phoneNumber = resources.getString(R.string.send_message_phone_number)
                val smsToSend = "Мои координаты: 50.450431, 30.521799"
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, smsToSend, null, null)
                Snackbar.make(v, R.string.snackbar_text, Snackbar.LENGTH_LONG).show()
                smsSent = true
                v.alpha = 0.5f
            } else {
                Snackbar.make(v, "Сообщение уже отправлено!", Snackbar.LENGTH_SHORT).show()
            }
        }
        infiniteAnimation()
        if (ContextCompat.checkSelfPermission(
                this@CallSOS,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@CallSOS,
                arrayOf(Manifest.permission.CALL_PHONE),
                PERMISSION_CODE
            )
        }
        if (ContextCompat.checkSelfPermission(
                this@CallSOS,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@CallSOS,
                arrayOf(Manifest.permission.SEND_SMS),
                PERMISSION_CODE
            )
        }
    }

    private fun infiniteAnimation() {
        val rotationAnim = ObjectAnimator.ofFloat(imageView, "rotation", -10f, 10f)
        val scaleXAnim = ObjectAnimator.ofFloat(imageView, "scaleX", 0.9f, 1.1f)
        val scaleYAnim = ObjectAnimator.ofFloat(imageView, "scaleY", 0.9f, 1.1f)
        rotationAnim.repeatCount = ObjectAnimator.INFINITE
        rotationAnim.repeatMode = ObjectAnimator.REVERSE
        scaleXAnim.repeatCount = ObjectAnimator.INFINITE
        scaleXAnim.repeatMode = ObjectAnimator.REVERSE
        scaleYAnim.repeatCount = ObjectAnimator.INFINITE
        scaleYAnim.repeatMode = ObjectAnimator.REVERSE
        animatorSet = AnimatorSet()
        animatorSet.playTogether(rotationAnim, scaleXAnim, scaleYAnim)
        animatorSet.setDuration(1000)
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.start()
    }

    private fun checkPermission(permission: String): Boolean {
        val check = ContextCompat.checkSelfPermission(this, permission)
        return check == PackageManager.PERMISSION_GRANTED
    }
}