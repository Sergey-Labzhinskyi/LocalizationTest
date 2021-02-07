package com.example.localizationtest

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        //inflate()
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)




        /* val layoutInflater = TranslationsLayoutInflater(LayoutInflater.from(baseContext), this)
         val v: View = layoutInflater.inflate(R.layout.activity_main, null)
         setContentView(v)*/


        // val wrappedContext: Context = TranslationContextWrapper(this)
        //  val v: View = LayoutInflater.from(wrappedContext).inflate(R.layout.activity_main, null)


        val text = findViewById<TextView>(R.id.tvText)
        //text.text = resources.getString(R.string.test)

        val a = findViewById<Button>(R.id.button)
        a.setOnClickListener {
            Log.d("Test", "${SystemClock.elapsedRealtime()/1000}")
        }

        val array = findViewById<TextView>(R.id.tvArray)
        array.text = resources.getStringArray(R.array.test_arrays)[0]
      //  Log.d("TestLocal", "onCreate ${resources.getStringArray(R.array.test_arrays).contentToString()}")


    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(TranslationContextWrapper(newBase!!, delegate /*other args*/))
    }

    override fun getResources(): Resources {
        return TranslationContextWrapper.TranslationResources(super.getResources())
    }

    private fun inflate() {
        val layoutInflater = LayoutInflater.from(this)
        layoutInflater.factory2 = object : LayoutInflater.Factory2 {
            override fun onCreateView(parent: View?,
                                      name: String,
                                      context: Context,
                                      attrs: AttributeSet): View? {
                Log.d("TestLocal", "MainActivity onCreate() name $name attrs $attrs")

                // if (name == "TextView" || name == "androidx.appcompat.widget.AppCompatTextView") {
                try {
                  //  val view = layoutInflater.createView(name, "android.widget.", attrs)
                    if ( name == "androidx.appcompat.widget.AppCompatTextView" ) {
                        return CustomAppCompatTextView(context, attrs)
                    }
                    //return delegate.createView(parent, name, context, attrs)
                } catch (e: ClassNotFoundException) {
                    Log.d("TestLocal", "CustomLayoutInflater onCreateView() ClassNotFoundException")

                } catch (inf: InflateException) {
                    Log.d("TestLocal", "CustomLayoutInflater onCreateView() InflateException")

                }
               //  return delegate.createView(parent, name, context, attrs)
                return null
            }

            override fun onCreateView(name: String,
                                      context: Context,
                                      attrs: AttributeSet): View? {
                return onCreateView(null, name, context, attrs)
            }
        }
    }
}