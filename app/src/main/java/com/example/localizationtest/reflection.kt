package com.example.localizationtest

import android.util.Log
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.lang.reflect.Method

internal fun Any.getMethod(name: String): ObjectMethod? =
    getMember { declaredMethods.find { it.name == name } }?.let { ObjectMethod(this, it) }

internal fun <T> Any.getField(fieldGetter: () -> Field) =
    runCatching { fieldGetter() }.getOrNull()?.let { ObjectField<T>(this, it) }

private fun <T : Member> Any.getMember(membersGetter: Class<Any>.() -> T?): T? = throughHierarchy { membersGetter() }

private fun <T> Any.throughHierarchy(invoke: Class<Any>.() -> T?): T? {
    var clz: Class<in Any>? = javaClass
    while (clz != null) {
        invoke(clz)?.let { return it }
        clz = clz.superclass
    }
    return null
}

internal class ObjectMethod(
    private val what: Any,
    private val method: Method) {
    operator fun invoke(vararg args: Any) = runCatching { method.withAccessible { invoke(what, *args) } }
        .onFailure {  }
}

internal class ObjectField<T>(
    private val what: Any,
    private val fld: Field
) {
    var value: T?
        get() = runCatching { fld.withAccessible { get(what) } }
            .onFailure { Log.e(it.message, "") }
            .getOrNull() as? T
        set(new) {
            runCatching { fld.withAccessible { set(what, new) } }
                .onFailure { Log.e(it.message, "") }
        }
}

private inline fun <T : AccessibleObject, R> T.withAccessible(block: T.() -> R): R {
    val prev = isAccessible
    isAccessible = true
    val result = runCatching { block() }
    isAccessible = prev
    return result.getOrThrow()
}