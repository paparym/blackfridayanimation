package com.practice.blackfridayanimation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.blackfridayanimation.databinding.TicketRowItemBinding
import kotlin.random.Random

class TicketsAdapter :
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
            binding.root.isVisible = ticketsRow.id != 0
            binding.tvId.text = ticketsRow.id.toString()
            binding.root.setBackgroundColor(Random.nextInt())
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
