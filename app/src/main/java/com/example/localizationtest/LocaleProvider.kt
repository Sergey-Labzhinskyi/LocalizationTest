package com.example.localizationtest

import java.util.*

interface LocaleProvider {
    val currentLocale: Locale
}

//Map<String, String> - is a converted tree of strings,
//which we received from backend to plain associated array

interface TranslationsCache {
    fun getTranslations(): Map<Locale, Map<String, String>>
    fun saveTranslations(obj: Map<Locale, Map<String, String>>)
}