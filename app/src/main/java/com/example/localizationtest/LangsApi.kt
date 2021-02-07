package com.example.localizationtest

interface LangsApi {

     fun getLanguages(): List<Language>

     fun getTranslations(locale: String): Any

}