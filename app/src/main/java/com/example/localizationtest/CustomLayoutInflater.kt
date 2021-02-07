package com.example.localizationtest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatTextView
import org.xmlpull.v1.XmlPullParser

class CustomLayoutInflater constructor(
        ordinal: LayoutInflater,
        context: Context,
        val delegate: AppCompatDelegate?
): LayoutInflater(ordinal, context) {


    private var privateFactorySet: Boolean = false

    override fun cloneInContext(newContext: Context?): LayoutInflater {
        return  CustomLayoutInflater(this, newContext!!, delegate /*other args*/)
    }

    override fun onCreateView(name: String?, attrs: AttributeSet?): View {
        Log.d("TestLocal", "CustomLayoutInflater onCreateView() $name")
        try {
            val view = createView(name, "android.widget.", attrs)
            if (view is TextView) {
                Log.d("TestLocal", "CustomLayoutInflater onCreateView() if")
                return overrideTextView(view, attrs)
            }
        }catch (e: ClassNotFoundException){
            Log.d("TestLocal", "CustomLayoutInflater onCreateView() ClassNotFoundException")

        }catch (inf: InflateException){
            Log.d("TestLocal", "CustomLayoutInflater onCreateView() InflateExceptionf")

        }
        return super.onCreateView(name, attrs)
    }

    private fun overrideTextView(view: TextView, attrs: AttributeSet?): TextView {
        Log.d("TestLocal", "CustomLayoutInflater overrideTextView()")
        val typedArray = view.context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.text))
        val stringResource = typedArray.getResourceId(0, -1)
        view.text = view.resources.getText(stringResource)
        typedArray.recycle()
        return view
    }

   /* override fun onCreateView(parent: View?, name: String?, attrs: AttributeSet?): View {
        Log.d("TestLocal", "CustomLayoutInflater onCreateView()2 ")
        return super.onCreateView(parent, name, attrs)
    }*/


   /* override fun setFactory(factory: Factory?) {
        Log.d("TestLocal", "CustomLayoutInflater setFactory() ")
        super.setFactory(factory)
    }*/

    override fun setFactory2(factory: Factory2?) {
        Log.d("TestLocal", "CustomLayoutInflater setFactory2() ")
        val final = factory?.let { (it as? WrapperFactory2) ?: WrapperFactory2(it, this) }
        super.setFactory2(final)
       // super.setFactory2(factory)
    }


   /* override fun inflate(resource: Int, root: ViewGroup?): View {
       // Log.d("TestLocal", "CustomLayoutInflater inflate1() ")
        return super.inflate(resource, root)
    }

    override fun inflate(parser: XmlPullParser?, root: ViewGroup?): View {
        //Log.d("TestLocal", "CustomLayoutInflater inflate2() ")
        return super.inflate(parser, root)
    }

    override fun inflate(resource: Int, root: ViewGroup?, attachToRoot: Boolean): View {
       // Log.d("TestLocal", "CustomLayoutInflater inflate3() ")
        return super.inflate(resource, root, attachToRoot)
    }*/

    override fun inflate(parser: XmlPullParser?, root: ViewGroup?, attachToRoot: Boolean): View {
        Log.d("TestLocal", "CustomLayoutInflater inflate4() ")
        setPrivateFactoryInternal()
        return super.inflate(parser, root, attachToRoot)
    }

    private fun setPrivateFactoryInternal() {
        Log.d("TestLocal", "TranslationsLayoutInflater setPrivateFactoryInternal()")
        if (privateFactorySet) return

        val ctx = context
        if (ctx !is Factory2) {
            privateFactorySet = true
            return
        }

        getMethod("setPrivateFactory")?.invoke(WrapperFactory2(PrivateFactory2(this, ctx), this /*args*/))
        privateFactorySet = true
    }

    private class WrapperFactory2(private val origin: Factory2,  val inflater: LayoutInflater?) : Factory2 {
        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "CustomLayoutInflater.WrapperFactory2 onCreateView()")
            return origin.onCreateView(name, context, attrs)
        }


        override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "CustomLayoutInflater.WrapperFactory2 onCreateView2() name $name attrs $attrs")

            try {
                if (name == "androidx.appcompat.widget.AppCompatTextView") {
                    return CustomAppCompatTextView(context, attrs)
                } else if (name == "androidx.appcompat.widget.AppCompatButton") {
                    return CustomAppCompatButton(context, attrs)
                }

            } catch (e: ClassNotFoundException) {
                Log.d("TestLocal", "CustomLayoutInflater onCreateView() ClassNotFoundException")

            } catch (inf: InflateException) {
                Log.d("TestLocal", "CustomLayoutInflater onCreateView() InflateException")

            }
            return null
        }
    }


    private class PrivateFactory2(
            private val inflater: LayoutInflater,
            private val origin: Factory2
    ) : Factory2 {
        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "CustomLayoutInflater.PrivateFactory2 onCreateView() name $name attrs $attrs")
            return origin.onCreateView(name, context, attrs)// ?: createCustomView(name, context, attrs)
        }


        override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
           // val view = createCustomView(name, context, attrs)
            Log.d("TestLocal", "CustomLayoutInflater.PrivateFactory2 onCreateView2() name $name ")
            return origin.onCreateView(name, context, attrs)
        }


    /*    private fun createCustomView(name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "CustomLayoutInflater.PrivateFactory2 createCustomView() name $name attrs $attrs")
            return name.takeIf { '.' in it }?.let { customViewName ->
                createCustomViewViaReflection(name, context, attrs)
              //  inflater.createView(context, customViewName, null, attrs)
                *//* if (atLeastQ()) {
                     inflater.createView(context, customViewName, null, attrs)
                 } else {
                     createCustomViewViaReflection(name, context, attrs)
                 }*//*
            }
        }*/

       /* private fun createCustomViewViaReflection(name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "CustomLayoutInflater.PrivateFactory2 createCustomViewViaReflection()")
            return inflater.getField<Array<Any>> { LayoutInflater::class.java.getField("mConstructorArgs") }?.value?.let { constructionArgs ->
                val lastCtx = constructionArgs[0]
                constructionArgs[0] = context
                val result = runCatching { inflater.createView(name, "androidx.appcompat.widget.", attrs) }
                constructionArgs[0] = lastCtx

                result.getOrNull()
            }
        }*/
    }


}