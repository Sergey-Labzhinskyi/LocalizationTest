package com.example.localizationtest

import android.util.AttributeSet
import android.util.Log
import android.view.View

class LayoutInterceptorCompositor (
    private val interceptors: Map<Class<out View>, LayoutInterceptor<out View>>
) : LayoutInterceptor<View> {
    override fun interceptAttrs(view: View, attrs: AttributeSet) {
        Log.d("TestLocal", "LayoutInterceptorCompositor interceptAttrs()")
        (peekInterceptor(view) as? LayoutInterceptor<View>)?.interceptAttrs(view, attrs)
    }

    private fun peekInterceptor(view: View) : LayoutInterceptor<out View>?{
        Log.d("TestLocal", "LayoutInterceptorCompositor peekInterceptor()")
       return interceptors[view::class.java] ?: searchAssignableInterceptor(view)
    }


    private fun searchAssignableInterceptor(view: View): LayoutInterceptor<out View>? {
        Log.d("TestLocal", "LayoutInterceptorCompositor searchAssignableInterceptor()")
        interceptors.forEach { (clz, interceptor) ->
            if (clz.isAssignableFrom(view::class.java)) {
                return interceptor
            }
        }

        return null
    }
}