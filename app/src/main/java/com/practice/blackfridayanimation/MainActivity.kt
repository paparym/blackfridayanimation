package com.practice.blackfridayanimation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.practice.blackfridayanimation.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var ticketsAdapter: TicketsAdapter
    private var animationState = TicketsAnimation.IN_PROGRESS

    private var ticketList = mutableListOf<Ticket>()
    private val dataFromApi = data10
    var job: Job? = null

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.recyclerView.itemAnimator = MyItemAnimator(this)
        val layoutManager = object : GridLayoutManager(this, 6) {
            override fun canScrollVertically(): Boolean {
                return true
            }
        }
        binding.recyclerView.layoutManager = layoutManager

        ticketsAdapter = TicketsAdapter()
        binding.recyclerView.adapter = ticketsAdapter

        binding.toolbar.navigationIcon?.setColorFilter(R.color.white, PorterDuff.Mode.LIGHTEN)

        job?.cancel()
        job = lifecycleScope.launch {
            dataFromApi.forEach { ticket ->
                addTicket(ticket)
                delay(DEFAULT_DURATION)
                updateTicketsCounted()
            }
            if (animationState == TicketsAnimation.IN_PROGRESS) completeAnimations()
        }

        binding.btnSkipOrContinue.setOnClickListener {
            animationState = TicketsAnimation.COMPLETED
            job?.cancel()
            ticketList.clear()
            ticketsAdapter.submitList(dataFromApi.reversed())
            binding.recyclerView.itemAnimator = null
            completeAnimations()

            binding.recyclerView.post {
                layoutManager.scrollToPosition(0)
            }
        }
    }

    private fun addTicket(ticket: Ticket) {
        val newList = ArrayList(ticketList)
        newList.add(0, ticket)
        ticketList = newList
        ticketsAdapter.submitList(ticketList)
    }

    private fun updateTicketsCounted() {
        binding.toolbarTitle.text = "Counting tickets (${ticketList.size - 1})"
    }

    private fun completeAnimations() {
        fadeAndChangeText(
            view = binding.toolbarTitle,
            newText = "Counting Completed (${dataFromApi.size - 1})"
        )
        fadeAndChangeText(view = binding.tvSkip, newText = "CONTINUE")
    }

    private fun fadeAndChangeText(view: TextView, newText: String) {
        view.animate()
            .alpha(0f)
            .setInterpolator(LinearInterpolator())
            .withEndAction {
                view.text = newText
                view.animate()
                    .setInterpolator(LinearInterpolator())
                    .alpha(1f)
                    .start()
            }
            .start()
    }

    enum class TicketsAnimation {
        IN_PROGRESS,
        COMPLETED
    }
}

class MyItemAnimator(context: Context) : DefaultItemAnimator() {

    private val defaultPadding = convertDpToPixel(4f, context) * 2

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val animatorSet = AnimatorSet()
        val view = holder.itemView
        view.x = -view.width.toFloat() - defaultPadding
        view.alpha = 0f

        val alphaAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 1f).apply {
            duration = DEFAULT_DURATION / 2
            startDelay = DEFAULT_DURATION / 2
        }
        val translationXAnim =
            ObjectAnimator.ofFloat(view, View.TRANSLATION_X, defaultPadding / 2).apply {
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
                    .setInterpolator(LinearInterpolator())
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
}

private const val DEFAULT_DURATION = 300L
