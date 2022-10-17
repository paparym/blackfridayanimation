package com.practice.blackfridayanimation.total_earned

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.practice.blackfridayanimation.R
import com.practice.blackfridayanimation.inflate

class TotalEarnedController : Controller() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        return inflater.inflate<TotalEarnedView>(R.layout.total_earned_view, container)
    }
}
