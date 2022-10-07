package com.practice.blackfridayanimation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Conductor.attachRouter
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.Router.PopRootControllerMode
import com.bluelinelabs.conductor.RouterTransaction
import com.bumptech.glide.Glide

class MainActivity : ComponentActivity() {
    private var router: Router? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_main)
        val container = findViewById<FrameLayout>(R.id.conductorContainer)
        router = attachRouter(this, container, savedInstanceState)
            .setPopRootControllerMode(PopRootControllerMode.NEVER)
        if (!router!!.hasRootController()) {
            router!!.setRoot(RouterTransaction.with(MainController()))
        }
    }

    override fun onBackPressed() {
        if (!router!!.handleBack()) {
            super.onBackPressed()
        }
    }
}

class MyItemAnimator(context: Context) : DefaultItemAnimator() {

    private val defaultPadding = convertDpToPixel(2f, context)

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val animatorSet = AnimatorSet()
        val view = holder.itemView
        view.x = -view.width.toFloat() - defaultPadding
        view.alpha = 0.5f

        val alphaAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 1f).apply {
            duration = DEFAULT_DURATION / 2
            startDelay = DEFAULT_DURATION / 2
        }
        val translationXAnim =
            ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f).apply {
                duration = DEFAULT_DURATION
            }

        animatorSet.apply {
            interpolator = LinearInterpolator()
            playTogether(translationXAnim, alphaAnim)
            start()
        }
        return true
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder,
        fromXParent: Int,
        fromYParent: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        val view = holder.itemView
        return if (holder.layoutPosition % 6 == 0 && holder.layoutPosition != 0) {
            handleItemRowChange(view, fromXParent, fromYParent, toX, toY)
        } else {
            handleItemMove(view, fromXParent, fromYParent, toX)
        }
    }

    private fun handleItemRowChange(
        view: View,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        val recyclerContainer = view.rootView.findViewById<FrameLayout>(R.id.recyclerContainer)
        val helperView = ImageView(view.context)
        if (view.visibility != View.GONE) {
            helperView.layoutParams = ViewGroup.LayoutParams(
                view.width,
                view.height
            )
            recyclerContainer.addView(helperView)
            helperView.y = toY.toFloat()
            helperView.x = -view.width - defaultPadding / 2
            helperView.alpha = 0.5f
            helperView.animate()
                .withStartAction {
                    Glide.with(recyclerContainer)
                        .load(loadBitmapFromView(view))
                        .into(helperView)
                }
//                .withEndAction {
//                    recyclerContainer.removeView(helperView)
//                }
                .alpha(1f)
                .translationX(toX.toFloat())
                .setDuration(DEFAULT_DURATION)
                .setInterpolator(LinearInterpolator())
                .start()
        }

        view.x = fromX.toFloat()
        view.y = fromY.toFloat()
        view.animate()
            .setInterpolator(LinearInterpolator())
            .translationXBy(view.width + (defaultPadding * 3))
            .setDuration(DEFAULT_DURATION)
            .alpha(0.5f)
            .withEndAction {
                recyclerContainer.removeView(helperView)
                view.alpha = 1f
            }
            .start()
        return false
    }

    private fun handleItemMove(
        view: View,
        fromX: Int,
        fromY: Int,
        toX: Int
    ): Boolean {
        view.x = fromX.toFloat()
        view.y = fromY.toFloat()
        view.animate()
            .setInterpolator(LinearInterpolator())
            .translationXBy(toX - fromX.toFloat())
            .setDuration(DEFAULT_DURATION)
            .start()
        return false
    }

    private fun convertDpToPixel(dp: Float, context: Context) =
        dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

    private fun loadBitmapFromView(v: View): Bitmap? {
        val b = Bitmap.createBitmap(
            v.width,
            v.height,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }
}

const val DEFAULT_DURATION = 500L

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
