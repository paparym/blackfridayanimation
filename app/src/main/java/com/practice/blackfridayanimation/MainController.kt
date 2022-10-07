package com.practice.blackfridayanimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller

class MainController : Controller() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = inflater.inflate<TicketView>(R.layout.ticket_view, container)
}
