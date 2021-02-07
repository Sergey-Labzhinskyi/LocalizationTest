package com.example.localizationtest

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

class CustomAppCompatTextView : AppCompatTextView {
    constructor(context: Context) : super(context) { initialize(null) }

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) {
        initialize(attrs) }

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) :
            super(context, attr, defStyleAttr) { initialize(attr) }

    private fun initialize(attr: AttributeSet?) {
        val typedArray = this.context.obtainStyledAttributes(attr, intArrayOf(android.R.attr.text))
        val stringResource = typedArray.getResourceId(0, -1)
        this.text = this.resources.getText(stringResource)
        typedArray.recycle()
    }
}