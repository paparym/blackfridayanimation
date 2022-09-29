package com.practice.blackfridayanimation

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
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
        holder.itemView.x = -holder.itemView.width.toFloat() - defaultPadding
        holder.itemView.alpha = 0f

        val view = holder.itemView
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
        return if (holder.layoutPosition % 6 == 0 && holder.layoutPosition != 0) {
            handleItemRowChange(holder, fromXParent, fromYParent, toX, toY)
        } else {
            handleItemMove(holder, fromXParent, fromYParent, toX, toY)
        }
    }

    private fun handleItemRowChange(
        holder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        val view = holder.itemView
        view.x = fromX.toFloat()
        view.y = fromY.toFloat()
        view.animate()
            .setInterpolator(LinearInterpolator())
            .translationXBy(view.width + defaultPadding * 2)
            .alpha(0.5f)
            .setDuration(DEFAULT_DURATION)
            .withEndAction {
                view.y = toY.toFloat()
                view.x = toX.toFloat()
                view.alpha = 1f
            }.start()
        return false
    }

    private fun handleItemMove(
        holder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        var fromX = fromX
        val view = holder.itemView
        fromX += view.translationX.toInt()

        val deltaX = toX - fromX
        if (deltaX == 0) {
            dispatchMoveFinished(holder)
            return false
        } else {
            view.translationX = -deltaX.toFloat()
        }
        view.x = fromX.toFloat()
        view.y = fromY.toFloat()
        view.animate()
            .setInterpolator(LinearInterpolator())
            .translationXBy(toX - fromX.toFloat())
            .setDuration(DEFAULT_DURATION)
            .withEndAction {
                view.y = toY.toFloat()
                view.x = toX.toFloat()
            }.start()
        return true
    }

    override fun getAddDuration() = DEFAULT_DURATION
    override fun getMoveDuration() = DEFAULT_DURATION

    private fun convertDpToPixel(dp: Float, context: Context) =
        dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

private const val DEFAULT_DURATION = 2000L
