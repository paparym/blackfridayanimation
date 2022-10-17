package com.practice.blackfridayanimation.available

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.practice.blackfridayanimation.R
import com.practice.blackfridayanimation.inflate

class AvailableController : Controller() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        return inflater.inflate<AvailableView>(R.layout.available_view, container)
    }
}
