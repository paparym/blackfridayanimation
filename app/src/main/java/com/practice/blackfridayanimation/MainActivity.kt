package com.practice.blackfridayanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var ticketsAdapter: TicketsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)

        ticketsAdapter = TicketsAdapter()
        recycler.adapter = ticketsAdapter
        ticketsAdapter.submitList(
            listOf(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
            )
        )
        lifecycleScope.launch {
            delay(100)
            recycler.x = -recycler.width.toFloat()
            recycler.animate()
                .setDuration(3000)
                .translationX(0f)
                .start()
        }
    }
}
