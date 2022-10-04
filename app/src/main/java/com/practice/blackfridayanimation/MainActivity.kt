package com.practice.blackfridayanimation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.practice.blackfridayanimation.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var ticketsAdapter: TicketsAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var animationState = TicketsAnimation.IN_PROGRESS

    private var ticketList = mutableListOf<Ticket>()
    private val dataFromApi = data1000
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

    override fun onPause() {
        forceCompleteAnimation()
        super.onPause()
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
                binding.recyclerView.itemAnimator = null
                completeAnimations()
                setCanScrollVertically(true)
            }
        }

        binding.btnSkipOrContinue.setOnClickListener {
            forceCompleteAnimation()
        }
    }

    private fun forceCompleteAnimation() {
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

private const val DEFAULT_DURATION = 2000L
