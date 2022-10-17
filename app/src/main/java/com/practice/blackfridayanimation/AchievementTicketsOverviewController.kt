package com.practice.blackfridayanimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller

class AchievementTicketsOverviewController : Controller() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ) = inflater.inflate<AchievementTicketsPagerView>(
        R.layout.achievement_ticekts_pager_view,
        container
    )
}
