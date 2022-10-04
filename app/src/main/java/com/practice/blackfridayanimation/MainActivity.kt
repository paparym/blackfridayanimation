package com.practice.blackfridayanimation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import by.kirich1409.viewbindingdelegate.viewBinding
import com.practice.blackfridayanimation.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var ticketsAdapter: TicketsAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var animationState = TicketsAnimation.IN_PROGRESS

    private var ticketList = mutableListOf<Ticket>()
    private val dataFromApi = data50
    var job: Job? = null

    private val binding by viewBinding(ActivityMainBinding::bind)

    private fun setCanScrollVertically(canScroll: Boolean) {
        layoutManager = object : GridLayoutManager(this, 6) {
            override fun canScrollVertically(): Boolean {
                return canScroll
            }
        }
        binding.recyclerView.layoutManager = layoutManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setCanScrollVertically(false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.itemAnimator = MyItemAnimator(this)

        ticketsAdapter = TicketsAdapter()
        val adapterObserver = object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.recyclerView.scrollToPosition(0)
            }
        }
        ticketsAdapter.registerAdapterDataObserver(adapterObserver)
        binding.recyclerView.adapter = ticketsAdapter

        binding.toolbar.navigationIcon?.setColorFilter(R.color.white, PorterDuff.Mode.LIGHTEN)

        job?.cancel()
        job = lifecycleScope.launch {
            dataFromApi.forEach { ticket ->
                addTicket(ticket)
                delay(DEFAULT_DURATION)
                updateTicketsCounted()
            }
            if (animationState == TicketsAnimation.IN_PROGRESS) {
                completeAnimations()
                setCanScrollVertically(true)
            }
        }

        binding.btnSkipOrContinue.setOnClickListener {
            animationState = TicketsAnimation.COMPLETED
            job?.cancel()
            ticketList.clear()
            binding.recyclerView.itemAnimator = null
            ticketsAdapter.submitList(dataFromApi.reversed())
            completeAnimations()

            setCanScrollVertically(true)

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

    private val defaultPadding = convertDpToPixel(2f, context)

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
        val newView = View(view.context)
        newView.layoutParams = ViewGroup.LayoutParams(
            view.width,
            view.height
        )
        (view.rootView as ViewGroup).addView(newView)
        newView.setBackgroundColor(R.color.white)
        newView.y = convertDpToPixel(24f, view.context) + toY + view.height + defaultPadding * 3
        newView.x = -view.width - defaultPadding / 2
        newView.animate()
            .setStartDelay(DEFAULT_DURATION)
            .translationX(toX.toFloat() + defaultPadding)
            .setDuration(DEFAULT_DURATION)
            .setInterpolator(LinearInterpolator())
            .withEndAction {
                (view.rootView as ViewGroup).removeView(newView)
            }
            .start()

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

private const val DEFAULT_DURATION = 2000L
