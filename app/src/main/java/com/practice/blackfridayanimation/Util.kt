package com.practice.blackfridayanimation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
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

fun LazyGridScope.informationHeader(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

fun LazyGridState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
