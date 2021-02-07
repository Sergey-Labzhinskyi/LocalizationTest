package com.example.localizationtest

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleableRes
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@SuppressWarnings("Recycle")
@UseExperimental(ExperimentalContracts::class)
inline fun AttributeSet.parseAttrs(context: Context, @StyleableRes attrs: IntArray, parser: TypedArray.() -> Unit) {
    contract { callsInPlace(parser, InvocationKind.EXACTLY_ONCE) }
    context.obtainStyledAttributes(this, attrs).use(parser)
}

@UseExperimental(ExperimentalContracts::class)
inline fun TypedArray.use(block: TypedArray.() -> Unit) {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    try {
        block()
    } finally {
        recycle()
    }
}

fun TypedArray.getResString(ctx: Context, index: Int): String? {
    return if (hasValue(index)) {
        getResourceId(index)?.let { ctx.getString(it) }
    } else {
        null
    }
}

fun TypedArray.getResourceId(index: Int): Int? = getResourceId(index, 0).takeIf { it > 0 }
