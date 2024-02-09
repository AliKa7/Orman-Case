package com.example.ormancase4

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    lateinit var logoImage: ImageView
    lateinit var welcomeText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        logoImage = findViewById(R.id.logoImage)
        welcomeText = findViewById(R.id.welcome)
        logoImage.alpha = 0.1f
        welcomeText.alpha = 0.1f
        logoImage.animate().alpha(1f).setDuration(1000)
        welcomeText.animate().alpha(1f).setDuration(1000)
        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 1200)
    }
}

