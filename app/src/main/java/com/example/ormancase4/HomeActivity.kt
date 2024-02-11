package com.example.ormancase4

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import java.util.Locale


class HomeActivity : AppCompatActivity() {
    private lateinit var toAdditionInfoButton: Button
    private lateinit var imageButton: ImageButton
    private lateinit var toCallSOSButton: Button
    private lateinit var toCaseButton: Button
    private lateinit var animatorSet: AnimatorSet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val spannableString = SpannableString(menuItem.title)
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this,
                        R.color.main_color
                    )
                ), 0, spannableString.length, 0
            )
            menuItem.title = spannableString
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        /*if (menu.javaClass.simpleName == "MenuBuilder") {
            try {
                val m: Method = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", Boolean::class.javaPrimitiveType)
                m.isAccessible = true
                m.invoke(menu, true)
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "onMenuOpened...unable to set icons for overflow menu", e)
            }
        }*/
        return super.onPrepareOptionsMenu(menu)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (getCurrentLanguage(this) == "kk") {
            setLocal(this@HomeActivity, "ru")
            item.title = "Поменять язык на казахский"
        } else {
            setLocal(this@HomeActivity, "kk")
            item.title = "Орыс тіліне аудару"
        }
        recreate()
        return true
    }

    private fun setLocal(context: Context, langCode: String) {
        Locale.setDefault(Locale(langCode))
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        resources.configuration.setLocale(Locale(langCode))
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun getCurrentLanguage(context: Context): String {
        val configuration = context.resources.configuration
        val currentLocaleList = ConfigurationCompat.getLocales(configuration)
        val currentLocale = currentLocaleList[0]
        if (currentLocale != null) {
            return currentLocale.language
        } else {
            return "salam"
        }
    }
}


