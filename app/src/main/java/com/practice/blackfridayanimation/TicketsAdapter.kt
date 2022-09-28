package com.practice.blackfridayanimation

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.blackfridayanimation.databinding.TicketRowItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TicketsAdapter(private val lifecycleScope: CoroutineScope) :
    ListAdapter<TicketRow, TicketsAdapter.TicketsViewHolder>(DiffUtilCallback()) {

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
        fun bind(ticketsRow: TicketRow) {
            ticketsRow.ticketList.forEach {
                // imageView
                val imageView = FrameLayout(binding.root.context)
                val viewWidth = getScreenWidth(binding.root.context) / 6
                binding.root.updateLayoutParams {
                    width = ticketsRow.ticketList.count() * viewWidth
                }
                imageView.setBackgroundColor(R.color.black)
                val imageViewParams: LinearLayout.LayoutParams =
                    LinearLayout.LayoutParams(viewWidth, MATCH_PARENT)
                imageViewParams.gravity = CENTER
                imageView.layoutParams = imageViewParams

                // textView
                val textView = TextView(imageView.context)
                textView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                textView.setTextColor(R.color.white)
                textView.text = it.id.toString()
                textView.gravity = CENTER

                binding.root.addView(imageView)
                imageView.addView(textView)

                lifecycleScope.launch(Dispatchers.Main) {
                    delay(200)
                    binding.root.x = -binding.root.width.toFloat()
                    binding.root.animate()
                        .translationX(0f)
                        .setDuration(3000)
                        .start()
                }
            }
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

    class DiffUtilCallback : DiffUtil.ItemCallback<TicketRow>() {
        override fun areItemsTheSame(oldItem: TicketRow, newItem: TicketRow): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TicketRow, newItem: TicketRow): Boolean {
            return oldItem == newItem
        }
    }
}
