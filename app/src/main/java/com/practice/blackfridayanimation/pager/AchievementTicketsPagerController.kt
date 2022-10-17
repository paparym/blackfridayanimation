package com.practice.blackfridayanimation.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.practice.blackfridayanimation.R
import com.practice.blackfridayanimation.inflate

class AchievementTicketsPagerController : Controller() {

    private lateinit var achievementTicketsAdapter: AchievementTicketsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        achievementTicketsAdapter = AchievementTicketsPagerAdapter(this)
        val view = inflater.inflate<AchievementTicketsPagerView>(
            R.layout.achievement_ticekts_pager_view,
            container
        )
        view.viewPager.adapter = achievementTicketsAdapter
        return view
    }
}
