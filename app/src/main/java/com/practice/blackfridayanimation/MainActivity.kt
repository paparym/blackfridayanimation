package com.practice.blackfridayanimation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var ticketsAdapter: TicketsAdapter

    private var ticketList = mutableListOf<Ticket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        recycler.itemAnimator = MyItemAnimator(this)
        val layoutManager = object : GridLayoutManager(this, 6) {
            override fun canScrollVertically(): Boolean {
                return true
            }
        }
        recycler.layoutManager = layoutManager

        ticketsAdapter = TicketsAdapter(lifecycleScope)
        recycler.adapter = ticketsAdapter
        lifecycleScope.launch {
            repeat(100) {
                val newList = ArrayList(ticketList)
                newList.add(0, Ticket(id = it))
                ticketList = newList
                ticketsAdapter.submitList(ticketList)
                delay(DEFAULT_DURATION)
            }
        }
    }
}

class MyItemAnimator(context: Context) : DefaultItemAnimator() {

    private val defaultPadding = convertDpToPixel(4f, context) * 2

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val view = holder.itemView
        view.x = -view.width.toFloat() - defaultPadding
        view.alpha = 0f

        val animation = view.animate()
        animation
            .alpha(1f)
            .setDuration(DEFAULT_DURATION)
            .setInterpolator(LinearInterpolator())
            .translationXBy(holder.itemView.width.toFloat() + defaultPadding * 2)
            .start()

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
        var fromX = fromXParent
        fromX += view.translationX.toInt()

        return if (holder.layoutPosition % 6 == 0 && holder.layoutPosition != 0) {
            handleItemRowChange(view, fromX, fromYParent, toX, toY)
        } else {
            handleItemMove(view, fromX, fromYParent, toX)
        }
    }

    private fun handleItemRowChange(
        view: View,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        view.x = fromX.toFloat()
        view.y = fromY.toFloat()
        view.animate()
            .setInterpolator(LinearInterpolator())
            .translationXBy(view.width / 2f + defaultPadding)
            .alpha(0.0f)
            .setDuration(DEFAULT_DURATION / 2)
            .withEndAction {
                view.y = toY.toFloat()
                view.x = -view.width / 2f
                view.animate()
                    .translationX(toX.toFloat())
                    .setDuration(DEFAULT_DURATION / 2)
                    .alpha(1f)
                    .start()
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

    private fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                currentWindowMetrics.bounds.width()
            } else {
                @Suppress("DEPRECATION")
                DisplayMetrics().also { metrics ->
                    defaultDisplay.getRealMetrics(metrics)
                }.widthPixels
            }
        }
    }
}

private const val DEFAULT_DURATION = 1000L
