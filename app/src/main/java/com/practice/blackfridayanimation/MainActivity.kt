package com.practice.blackfridayanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {

    private lateinit var ticketsAdapter: TicketsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)

        ticketsAdapter = TicketsAdapter(lifecycleScope)
        recycler.adapter = ticketsAdapter
        ticketsAdapter.submitList(
            listOf(
                TicketRow(
                    spanIndex = 0,
                    ticketList = listOf(
                        Ticket(1),
                        Ticket(2),
                        Ticket(3),
                        Ticket(4),
                        Ticket(5),
                        Ticket(6),
                        Ticket(7),
                        Ticket(8)
                    )
                ),
                TicketRow(
                    spanIndex = 1,
                    ticketList = listOf(
                        Ticket(7),
                        Ticket(8)
                    )
                )
            )
        )
//        lifecycleScope.launch {
//            delay(100)
//            recycler.x = -recycler.width.toFloat()
//            recycler.animate()
//                .setDuration(3000)
//                .translationX(0f)
//                .start()
//        }
    }
}
