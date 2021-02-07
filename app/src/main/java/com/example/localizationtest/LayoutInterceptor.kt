package com.example.localizationtest

import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

interface LayoutInterceptor<T : View> {
    fun interceptAttrs(view: T, attrs: AttributeSet)
}

inline fun <reified T : View> interceptor(
    crossinline block: (view: T, attrs: AttributeSet) -> Unit
): Pair<Class<T>, LayoutInterceptor<T>> = T::class.java to object : LayoutInterceptor<T> {
    override fun interceptAttrs(view: T, attrs: AttributeSet) = block(view, attrs)
}

fun toolbarInterceptor() = interceptor<Toolbar> { view, attrs ->
    Log.d("TestLocal", "LayoutInterceptor toolbarInterceptor()")
    attrs.parseAttrs(view.context, IntArray(1)) {
        getResString(view.context, 0)?.let { view.title = it }
        getResString(view.context, 1)?.let { view.title = it }
    }
}

fun textViewInterceptor() = interceptor<TextView> { view, attrs ->
    Log.d("TestLocal", "LayoutInterceptor textViewInterceptor()")
    attrs.parseAttrs(view.context, IntArray(2) ) {
        getResString(view.context, 0)?.let { view.text = it }
        getResString(view.context, 1)?.let { view.hint = it }
    }
}