package com.practice.blackfridayanimation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

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

inline var Router.root: Controller?
    set(value) {
        if (value != null) {
            setRoot(RouterTransaction.with(value))
        }
    }
    get() = if (backstackSize > 0) backstack[0].controller else null
