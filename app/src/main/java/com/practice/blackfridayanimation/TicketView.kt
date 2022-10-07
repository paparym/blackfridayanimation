package com.practice.blackfridayanimation

import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.blackfridayanimation.databinding.TicketViewBinding
import kotlinx.coroutines.*

class TicketView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: TicketViewBinding

    private lateinit var ticketsAdapter: TicketsAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var animationState = TicketsAnimation.IN_PROGRESS

    private val dataFromApi = data10

    private var ticketList = mutableListOf<Ticket>(
        Ticket(
            id = -1
        )
    )
    private var job: Job? = null
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = TicketViewBinding.bind(this)

//        setCanScrollVertically(false)
        binding.recyclerView.itemAnimator =
            MyItemAnimator(binding.recyclerView.context)
        ticketsAdapter = TicketsAdapter()
        ticketsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.recyclerView.scrollToPosition(FIRST_ITEM_POSITION)
            }
        })
        binding.recyclerView.adapter = ticketsAdapter

        job?.cancel()
        job = coroutineScope.launch {
            dataFromApi.forEach { ticket ->
                addTicket(ticket)
                delay(DEFAULT_DURATION)
                // updateTicketsCounted()
            }
            if (animationState == TicketsAnimation.IN_PROGRESS) {
                binding.recyclerView.itemAnimator = null
                completeAnimations()
                // setCanScrollVertically(true)
            }
        }
    }

//    override fun render(state: AchievementTicketsCountState) {
//        job?.cancel()
//        job = coroutineScope.launch {
//            if (state.tickets.isNotEmpty()) {
//                state.tickets.forEach { ticket ->
//                    addTicket(ticket)
//                    delay(DEFAULT_DURATION)
//                    // updateTicketsCounted()
//                }
//                if (animationState == TicketsAnimation.IN_PROGRESS) {
//                    binding.recyclerView.itemAnimator = null
//                    completeAnimations()
//                    // setCanScrollVertically(true)
//                }
//            }
//        }
//    }

    private fun addTicket(ticket: Ticket) {
        val newList = ArrayList(ticketList)
        newList.add(FIRST_ITEM_POSITION, ticket)
        ticketList = newList
        ticketsAdapter.submitList(ticketList)
    }

    private fun setCanScrollVertically(canScroll: Boolean) {
        layoutManager =
            object : GridLayoutManager(binding.recyclerView.context, DEFAULT_SPAN_COUNT) {
                override fun canScrollVertically(): Boolean {
                    return canScroll
                }
            }
        binding.recyclerView.layoutManager = layoutManager
    }

    // override fun onPause() {
    //   forceCompleteAnimation()
    //   super.onPause()
    // }

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

    private fun completeAnimations() {
        // fadeAndChangeText(
        //   view = binding.toolbarTitle,
        //   newText = "Counting Completed (${dataFromApi.size - 1})"
        // )
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

    companion object {
        private const val FIRST_ITEM_POSITION = 0
        private const val DEFAULT_SPAN_COUNT = 6
    }
}
