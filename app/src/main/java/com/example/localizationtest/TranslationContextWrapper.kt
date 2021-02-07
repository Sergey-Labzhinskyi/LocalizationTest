package com.example.localizationtest

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

class TranslationContextWrapper(
    base: Context,
   val delegate: AppCompatDelegate?
    //any custom arguments (for instance, cache of strings)
) : ContextWrapper(base) {

    private var inflater: LayoutInflater? = null
    private val resources by lazy { TranslationResources(super.getResources())}//.also { it.repository = repository } }
   // private lateinit var resources: TranslationResources

    override fun getResources(): Resources {

     //   Log.d("TestLocal", "TranslationContextWrapper getResources ${resources.getString(11)}")
        return resources
    }



   override fun getSystemService(name: String): Any? {
        if (LAYOUT_INFLATER_SERVICE == name) {
            if (inflater == null) {
                inflater =  CustomLayoutInflater(LayoutInflater.from(baseContext), this, delegate)
            }
            return inflater
        }
        return super.getSystemService(name);
    }

    class TranslationResources(base: Resources)
        : Resources(base.assets, base.displayMetrics, base.configuration) {
     //   var repository: TranslationRepository? = null


        override fun getText(id: Int): CharSequence {
            Log.d("TestLocal", "TranslationContextWrapper getText() ${getResourceName(id)}")
            //Log.d("TestLocal", "TranslationContextWrapper getText() ${getResourceEntryName(id)}")
            return  "Successful"
            //return  repository?.getTranslation(getResourceEntryName(id)) ?: super.getText(id)
        }

        override fun getString(id: Int): String {
            Log.d("TestLocal", "TranslationContextWrapper getString() ${getResourceName(id)}")
            return super.getString(id)
        }

        override fun getStringArray(id: Int): Array<String> {
            Log.d("TestLocal", "TranslationContextWrapper getStringArray() " +
                    "${super.getStringArray(id).contentToString()}")
            //return super.getStringArray(id)
            val resourceName = getResourceName(id).substringAfter("/")
            Log.d("TestLocal", "TranslationContextWrapper getStringArray() resourceName $resourceName")

            if (getResourceName(id).contains("test_arrays")) {
                return arrayOf("AUGood", "AGGood", "PTGood", "PDGood", "PDGood")
            }else{
                return super.getStringArray(id)
            }
        }

    }


}
