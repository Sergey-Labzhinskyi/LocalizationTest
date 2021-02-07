package com.example.localizationtest

import java.util.*
import kotlin.collections.HashMap

interface TranslationRepository {
    fun restore()
    suspend fun fetchData()
    fun getTranslation(key: String): String?
}

class ApiTranslationRepository(
    private val api: LangsApi,
    private val localeManager: LocaleProvider,
    private val cache: TranslationsCache
) : TranslationRepository {
    @Volatile
    private var visited: Boolean = false

    @Volatile
    private var langMap: Map<Locale, Map<String, String>> = emptyMap()

    override fun restore() {
        langMap = cache.getTranslations()
    }

    override suspend fun fetchData() {
    /*    val langs = api.runCall { getLanguages() }

        val newLangMap = HashMap<Locale, Map<String, String>>()
        langs?.forEach {
            api.runCall { getTranslations(it.locale) }?.let { translations ->
                newLangMap[Locale(it.locale)] = mapToLangMap(translations)
            } ?: return
        }

        cache.saveTranslations(newLangMap)

        if (!visited) {
            langMap = newLangMap
        }*/
    }

    override fun getTranslation(key: String): String? {
        visited = true
        return langMap[localeManager.currentLocale]?.get(key)
    }

  /*  private inline fun <R> LangsApi.runCall(apiCall: LangsApi.() -> R): R? =
        runCatching { apiCall() }.onFailure { Timber.e(it) }.getOrNull()*/

    private fun mapToLangMap(translations: Any): Map<String, String> {
        val result = HashMap<String, String>()
        if (translations is Map<*, *>) {
            val data = translations["data"]
            if (data is Map<*, *>) {
                traverse("", data, result)
            }
        }
        return result
    }

    private fun traverse(key: String, value: Map<*, *>, output: MutableMap<String, String>) {
        value.forEach { (subKey, subValue) ->
            val newKey = if (key.isNotEmpty()) "${key}_$subKey" else subKey.toString()
            if (subValue is Map<*, *>) {
                traverse(newKey, subValue, output)
            } else if (subValue != null) {
                output[newKey] = convert(subValue.toString()).replace("...", "…")
            }
        }
    }

    private fun convert(value: String): String {
        //make your conversions of some symbols, placeholders and etc here
        //for instance: if you need to replace '...' with '…', you need to do it here
        return "convert()"
    }
}