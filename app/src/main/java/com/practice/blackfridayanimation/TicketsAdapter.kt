package com.practice.blackfridayanimation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.blackfridayanimation.databinding.TicketItemBinding

class TicketsAdapter : ListAdapter<Int, TicketsAdapter.TicketsViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsViewHolder {
        return TicketsViewHolder(
            TicketItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TicketsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TicketsViewHolder(private val binding: TicketItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(number: Int) {
            binding.tvNumber.text = number.toString()
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }
}
