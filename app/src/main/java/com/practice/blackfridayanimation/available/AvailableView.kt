package com.practice.blackfridayanimation.available

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.unit.dp
import com.practice.blackfridayanimation.models.data50

class AvailableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private val dataFromApi = data50

    @Composable
    override fun Content() {
        LazyVerticalGrid(columns = GridCells.Fixed(6)) {
            items(dataFromApi) { ticket ->
                Box(
                    modifier = Modifier
                        .background(color = Color.Blue)
                        .padding(4.dp)
                )
            }
        }
    }
}
