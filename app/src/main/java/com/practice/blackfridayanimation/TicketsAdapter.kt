package com.practice.blackfridayanimation

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.blackfridayanimation.databinding.TicketRowItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlin.random.Random

class TicketsAdapter(private val lifecycleScope: CoroutineScope) :
    ListAdapter<Ticket, TicketsAdapter.TicketsViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsViewHolder {
        return TicketsViewHolder(
            TicketRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TicketsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TicketsViewHolder(private val binding: TicketRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ticketsRow: Ticket) {
            if (ticketsRow.id == 0) binding.root.visibility = View.GONE else View.VISIBLE
            binding.tvId.text = ticketsRow.id.toString()
            binding.root.setBackgroundColor(Random.nextInt())
        }
    }

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

    class DiffUtilCallback : DiffUtil.ItemCallback<Ticket>() {
        override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
