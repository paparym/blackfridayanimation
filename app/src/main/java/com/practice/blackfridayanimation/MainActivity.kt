package com.practice.blackfridayanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
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
            repeat(5) {
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
            .setDuration(addDuration)
            .setInterpolator(LinearInterpolator())
            .translationXBy(holder.itemView.width.toFloat() + defaultPadding * 2)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animator: Animator) {
                    Log.d("-->", "start")
                    dispatchAddStarting(holder)
                }

                override fun onAnimationEnd(animator: Animator) {
                    Log.d("-->", "end")
                    animation.setListener(null)
                    dispatchAddFinished(holder)
                }
            }).start()

        return true
    }

    override fun getAddDuration() = DEFAULT_DURATION
    override fun getMoveDuration() = DEFAULT_DURATION

    private fun convertDpToPixel(dp: Float, context: Context) =
        dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

private const val DEFAULT_DURATION = 1000L
