package com.practice.blackfridayanimation.total_earned

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.practice.blackfridayanimation.databinding.TotalEarnedViewBinding

class TotalEarnedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: TotalEarnedViewBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = TotalEarnedViewBinding.bind(this)
    }
}
