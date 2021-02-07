package com.example.localizationtest

import android.content.res.Resources
import android.util.AttributeSet
import android.util.Log

class Attr (
    private val origin: AttributeSet,
    private val resources: Resources
) : AttributeSet by origin {

    override fun getAttributeValue(index: Int): String {
        Log.d("TestLocal", "Attr getAttributeValue()")
        val originResult = origin.getAttributeValue(index)
        return if (originResult != null && originResult.startsWith("@string")) {
            resources.getString(origin.getAttributeResourceValue(index, 0))
        } else {
            originResult
        }
    }


}