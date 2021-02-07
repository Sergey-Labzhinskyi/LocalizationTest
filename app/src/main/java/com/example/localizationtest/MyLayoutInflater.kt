package com.example.localizationtest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View

class MyLayoutInflater(
        origin: LayoutInflater,
        context: Context,
        cloned: Boolean = false,
        //other args
) : LayoutInflater(origin, context) {


    override fun cloneInContext(newContext: Context?): LayoutInflater {
        Log.d("TestLocal", "MyLayoutInflater cloneInContext()")
        return  MyLayoutInflater(this, newContext!!, true, /*other args*/)
    }

    override fun setFactory(factory: Factory?) {
        Log.d("TestLocal", "MyLayoutInflater setFactory()")
        val final = factory?.let { (it as? WrapperFactory) ?: WrapperFactory(/*args*/) }
        super.setFactory(final)
    }

    override fun setFactory2(factory: Factory2?) {
        Log.d("TestLocal", "MyLayoutInflater setFactory()2")
        val final = factory?.let { (it as? WrapperFactory2) ?: WrapperFactory2(it) }
        super.setFactory2(final)
    }

    private class WrapperFactory() : Factory {

        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            //you intercept view creation here
            Log.d("TestLocal", "WrapperFactory onCreateView() name $name attrs $attrs")
            return null
        }
    }

    private class WrapperFactory2(private val origin: Factory2) : Factory2 {
        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "WrapperFactory2 onCreateView() name $name attrs $attrs")
            return  origin.onCreateView(name, context, attrs)
        }


        override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
            Log.d("TestLocal", "WrapperFactory2 onCreateView2() parent $parent name $name attrs $attrs")
            return origin.onCreateView(parent, name, context, attrs)
        }
    }

}
