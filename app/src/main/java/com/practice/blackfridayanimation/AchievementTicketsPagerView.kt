package com.practice.blackfridayanimation

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.practice.blackfridayanimation.databinding.AchievementTicektsPagerViewBinding

class AchievementTicketsPagerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: AchievementTicektsPagerViewBinding
    private val viewPager get() = binding.viewPager
    private val tabLayout get() = binding.tabLayout

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = AchievementTicektsPagerViewBinding.bind(this)
        tabLayout.setupWithViewPager(viewPager)
    }
}
