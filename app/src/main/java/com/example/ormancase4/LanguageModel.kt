package com.example.ormancase4

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import java.util.Locale

class LanguageModel {
    companion object {
        @JvmStatic
        fun getCurrentLanguage(context: Context): String {
            val configuration = context.resources.configuration
            val currentLocaleList = ConfigurationCompat.getLocales(configuration)
            val currentLocale = currentLocaleList[0]
            if (currentLocale != null) {
                return currentLocale.language
            } else {
                return "salam"
            }
        }
        @JvmStatic
        fun setLocal(context: Context, langCode: String) {
            Locale.setDefault(Locale(langCode))
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            resources.configuration.setLocale(Locale(langCode))
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }
}