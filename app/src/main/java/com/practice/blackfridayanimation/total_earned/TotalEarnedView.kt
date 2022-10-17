package com.practice.blackfridayanimation.total_earned

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.unit.dp
import com.practice.blackfridayanimation.models.data50

class TotalEarnedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private val dataFromApi = data50

    @Composable
    override fun Content() {
        Column(
            Modifier.padding(
                top = 32.dp,
                end = 16.dp,
                start = 16.dp
            )
        ) {
            Text(
                text = "Earn tickets by participating in Black Friday events. Tap to learn more",
                color = Color.White
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier.padding(top = 32.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)

            ) {
                items(dataFromApi) { ticket ->
                    Box(
                        modifier = Modifier
                            .height(82.dp)
                            .background(color = Color.Blue)
                    )
                }
            }
        }
    }
}
