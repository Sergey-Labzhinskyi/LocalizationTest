package com.example.localizationtest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.xmlpull.v1.XmlPullParser

internal class TranslationsLayoutInflater(
    origin: LayoutInflater,
    context: Context,
    cloned: Boolean = false,
    private val customInterceptors: Map<Class<out View>, LayoutInterceptor<out View>> = mapOf()
    //other args
) : LayoutInflater(origin, context) {

    private var privateFactorySet: Boolean = false
    private val interceptor = LayoutInterceptorCompositor(hashMapOf(
        textViewInterceptor(),
        toolbarInterceptor()
    ).also { it.putAll(customInterceptors) })

    init {
        Log.d("TestLocal", "TranslationsLayoutInflater init()")
        if (!cloned) {
            factory?.let { factory = it }
            factory2?.let { factory2 = it }
        }
    }

    override fun cloneInContext(newContext: Context) :TranslationsLayoutInflater {
        Log.d("TestLocal", "TranslationsLayoutInflater cloneInContext()")
      return  TranslationsLayoutInflater(this, newContext, true, /*other args*/)
    }

    override fun setFactory(factory: Factory?) {
        Log.d("TestLocal", "TranslationsLayoutInflater setFactory()")
        val final = factory?.let { (it as? WrapperFactory) ?: WrapperFactory(/*args*/) }
        super.setFactory(final)
    }

    override fun setFactory2(factory: Factory2?) {
        Log.d("TestLocal", "TranslationsLayoutInflater setFactory2()")
        val final = factory?.let { (it as? WrapperFactory2) ?: WrapperFactory2(it) }
        super.setFactory2(final)
    }

    override fun inflate(parser: XmlPullParser, root: ViewGroup?, attachToRoot: Boolean): View {
        Log.d("TestLocal", "TranslationsLayoutInflater inflate() parser: $parser root: $root, attachToRoot: $attachToRoot")
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

        getMethod("setPrivateFactory")?.invoke(WrapperFactory2(PrivateFactory2(this, ctx) /*args*/))
        privateFactorySet = true
    }

    private class WrapperFactory(
        //..args
    ) : Factory {

        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            //you intercept view creation here
            Log.d("TestLocal", "WrapperFactory onCreateView()")
            return null
        }

    }

    private class WrapperFactory2(private val origin: Factory2) : Factory2 {
        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "WrapperFactory2 onCreateView()")
            return  origin.onCreateView(name, context, attr(name, attrs, context))
        }


        override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "WrapperFactory2 onCreateView2() name $name attrs $attrs")
            val at = attr(name, attrs, context)
            Log.d("TestLocal", "WrapperFactory2 onCreateView2() at $at ")
            return origin.onCreateView(parent, name, context, at)
        }
    }

    private class PrivateFactory2(
        private val inflater: LayoutInflater,
        private val origin: Factory2
    ) : Factory2 {
        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "PrivateFactory2 onCreateView() name $name attrs $attrs")
        return origin.onCreateView(name, context, attrs) ?: createCustomView(name, context, attrs)
        }


        override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "PrivateFactory2 onCreateView2() name $name attrs $attrs")
            return origin.onCreateView(name, context, attrs) ?: createCustomView(name, context, attrs)
        }


        private fun createCustomView(name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "PrivateFactory2 createCustomView() name $name attrs $attrs")
            return name.takeIf { '.' in it }?.let { customViewName ->
                inflater.createView(context, customViewName, null, attrs)
               /* if (atLeastQ()) {
                    inflater.createView(context, customViewName, null, attrs)
                } else {
                    createCustomViewViaReflection(name, context, attrs)
                }*/
            }
        }

        private fun createCustomViewViaReflection(name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "PrivateFactory2 createCustomViewViaReflection()")
            return inflater.getField<Array<Any>> { LayoutInflater::class.java.getField("mConstructorArgs") }?.value?.let { constructionArgs ->
                val lastCtx = constructionArgs[0]
                constructionArgs[0] = context
                val result = runCatching { inflater.createView(name, null, attrs) }
                constructionArgs[0] = lastCtx

                result.getOrNull()
            }
        }
    }

    companion object {
        private val NAMES = arrayOf("TextView", "ImageView", "Button", "EditText", "Spinner",
            "ImageButton", "CheckBox", "RadioButton", "CheckedTextView", "MultiAutoCompleteTextView",
            "AutoCompleteTextView", "RatingBar", "SeekBar", "ToggleButton")

        fun attr(name: String, attrs: AttributeSet, context: Context): AttributeSet{
            Log.d("TestLocal", "companion attr()")
            return  if (name in NAMES) attrs else Attr(attrs, context.resources)
        }

    }

    override fun onCreateView(name: String?, attrs: AttributeSet?): View {
        return super.onCreateView(name, attrs)
    }
}