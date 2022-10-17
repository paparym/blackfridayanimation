package com.practice.blackfridayanimation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

inline fun <reified V : View> LayoutInflater.inflate(
    @LayoutRes layoutId: Int,
    container: ViewGroup,
    block: V.() -> Unit = {}
): V {
    val view = inflate(layoutId, container, false)
    if (view !is V) {
        val layoutName = container.resources.getResourceName(layoutId)
        throw ClassCastException("Root tag of $layoutName is not ${V::class.java.canonicalName}")
    }
    view.block()
    return view
}
