package com.practice.blackfridayanimation

import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
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
        recycler.itemAnimator = MyItemAnimator()

        ticketsAdapter = TicketsAdapter(lifecycleScope)
        recycler.adapter = ticketsAdapter
        lifecycleScope.launch {
            repeat(20) {
                val newList = ArrayList(ticketList)
                newList.add(0, Ticket(id = it))
                ticketList = newList
                ticketsAdapter.submitList(ticketList)
                delay(1000L)
            }
        }
    }
}

class MyItemAnimator : DefaultItemAnimator() {
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        Log.d("-->", "x: ${holder.itemView.x}")
        holder.itemView.x = -holder.itemView.width.toFloat() - 11f
        holder.itemView.animate()
            .setInterpolator(LinearInterpolator())
            .setDuration(1000L)
            .translationXBy(holder.itemView.width.toFloat() + 22f)
            .start()
        return true
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
//        val listFiltered =
//            list.filter { holder.bindingAdapterPosition % 5 == 0 && holder.bindingAdapterPosition != 0 }
//        val mappedList = listFiltered
//            .map { it + listFiltered.indexOf(it) }
        return if (holder.bindingAdapterPosition % 6 == 0 && holder.bindingAdapterPosition != 0) {
//            val view = holder.itemView
//            val deltaX = toX - fromX
//            val deltaY = toY - fromY
//            if (deltaX != 0) {
//                view.animate().translationX(0f)
//            }
//            if (deltaY != 0) {
//                view.animate().translationY(0f)
//            }
//
//            val animation = view.animate()
//            mMoveAnimations.add(holder)
//            animation.setDuration(moveDuration).setInterpolator(LinearInterpolator())
//                .setListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationStart(animator: Animator) {
//                        dispatchMoveStarting(holder)
//                    }
//
//                    override fun onAnimationCancel(animator: Animator) {
//                        if (deltaX != 0) {
//                            view.translationX = 0f
//                        }
//                        if (deltaY != 0) {
//                            view.translationY = 0f
//                        }
//                    }
//
//                    override fun onAnimationEnd(animator: Animator) {
//                        animation.setListener(null)
//                        dispatchMoveFinished(holder)
//                        mMoveAnimations.remove(holder)
//                        dispatchFinishedWhenDone()
//                    }
//                }).start()

            holder.itemView.x = fromX.toFloat()
            holder.itemView.y = fromY.toFloat()
//            holder.itemView.animate().translationX(0f)
            holder.itemView.animate()
                .setInterpolator(LinearInterpolator())
                .alpha(0.0f)
                .setDuration(1000L)
                .translationYBy(0f)
                .translationXBy(holder.itemView.width.toFloat() + 22f)
                .withEndAction {
                    holder.itemView.y = toY.toFloat()
                    holder.itemView.alpha = 1f
//                    holder.itemView.x = 0f - holder.itemView.width.toFloat()

                    holder.itemView.x = -holder.itemView.width.toFloat() - 11f
                    holder.itemView.animate()
                        .setInterpolator(LinearInterpolator())
                        .setDuration(1000L)
                        .translationXBy(holder.itemView.width.toFloat() + 22f)
                        .start()
                }
                .start()
            false
        } else {
            holder.itemView.x = fromX.toFloat()
            holder.itemView.y = fromY.toFloat()
//            holder.itemView.animate().translationX(0f)
            holder.itemView.animate()
                .setInterpolator(LinearInterpolator())
//                .alpha(0.0f)
                .setDuration(1000L)
                .translationYBy(0f)
                .translationXBy((toX - fromX).toFloat())
                .start()
            return false
        }
    }

    override fun getMoveDuration() = 1000L
}
