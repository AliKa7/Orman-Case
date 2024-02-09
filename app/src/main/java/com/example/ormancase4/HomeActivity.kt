package com.example.ormancase4

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {
    private lateinit var toAdditionInfoButton: Button
    private lateinit var imageButton: ImageButton
    private lateinit var toCallSOSButton: Button
    private lateinit var toCaseButton: Button
    private lateinit var animatorSet: AnimatorSet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        //toMainActivityButton = findViewById(R.id.toMainActivity);
        imageButton = findViewById(R.id.imageButton)
        val rotationAnim = ObjectAnimator.ofFloat(imageButton, "rotation", -10f, 10f)
        val scaleXAnim = ObjectAnimator.ofFloat(imageButton, "scaleX", 0.9f, 1.1f)
        val scaleYAnim = ObjectAnimator.ofFloat(imageButton, "scaleY", 0.9f, 1.1f)
        rotationAnim.repeatCount = ObjectAnimator.INFINITE
        rotationAnim.repeatMode = ObjectAnimator.REVERSE
        scaleXAnim.repeatCount = ObjectAnimator.INFINITE
        scaleXAnim.repeatMode = ObjectAnimator.REVERSE
        scaleYAnim.repeatCount = ObjectAnimator.INFINITE
        scaleYAnim.repeatMode = ObjectAnimator.REVERSE
        animatorSet = AnimatorSet()
        animatorSet.playTogether(rotationAnim, scaleXAnim, scaleYAnim)
        animatorSet.setDuration(2000)
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.start()
        imageButton.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    MainActivity::class.java
                )
            )
        }
        toCallSOSButton = findViewById(R.id.toCallSOSButton)
        toCallSOSButton.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    CallSOS::class.java
                )
            )
        }
        toAdditionInfoButton = findViewById(R.id.toAddnInfoButton)
        toAdditionInfoButton.setOnClickListener {
            val intent = Intent(this@HomeActivity, AdditionalInfo::class.java)
            startActivity(intent)
        }
        toCaseButton = findViewById(R.id.toCaseButton)
        toCaseButton.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    CaseInfo::class.java
                )
            )
        }
    }
}

