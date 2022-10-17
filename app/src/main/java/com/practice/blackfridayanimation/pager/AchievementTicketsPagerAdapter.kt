package com.practice.blackfridayanimation.pager

import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.viewpager.RouterPagerAdapter
import com.practice.blackfridayanimation.available.AvailableController
import com.practice.blackfridayanimation.root
import com.practice.blackfridayanimation.total_earned.TotalEarnedController

class AchievementTicketsPagerAdapter(
    host: AchievementTicketsPagerController
) : RouterPagerAdapter(host) {

    private val sections = AchievementTicketsSection.values()

    override fun getCount() = 2

    override fun configureRouter(router: Router, position: Int) {
        if (!router.hasRootController()) {
            router.root = when (sections[position]) {
                AchievementTicketsSection.TOTAL_EARNED -> TotalEarnedController()
                AchievementTicketsSection.AVAILABLE -> AvailableController()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (sections[position]) {
            AchievementTicketsSection.TOTAL_EARNED -> "Total Earned"
            AchievementTicketsSection.AVAILABLE -> "Available"
        }
    }
}
