package com.practice.blackfridayanimation.available

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.practice.blackfridayanimation.databinding.AvailableViewBinding

class AvailableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: AvailableViewBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = AvailableViewBinding.bind(this)
    }
}
