package com.practice.blackfridayanimation.total_earned

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.practice.blackfridayanimation.informationHeader
import com.practice.blackfridayanimation.isScrolledToTheEnd
import com.practice.blackfridayanimation.models.AchievementTicket
import com.practice.blackfridayanimation.models.data50
import com.practice.blackfridayanimation.total_earned.TotalEarnedTicketsView.Companion.HEIGHT_TICKET
import com.practice.blackfridayanimation.total_earned.TotalEarnedTicketsView.Companion.PADDING_HEADER_VERTICAL

class TotalEarnedTicketsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    @Composable
    override fun Content() {
        var dataFromApi by remember { mutableStateOf(data50) }
        val listState = rememberLazyGridState()
        TotalEarnedScreen(
            data = dataFromApi,
            listState = listState,
            onEndReached = {
                dataFromApi = dataFromApi + data50
                Log.d("-->", "Paging worked new size:")
            }
        )
    }

    companion object {
        internal const val ITEM_PADDING = 4
        internal const val LIST_PADDING = 16
        internal const val SPAN_COUNT = 6
        internal const val PADDING_HEADER_VERTICAL = 32
        internal const val HEIGHT_TICKET = 82
    }
}

@Composable
private fun TotalEarnedInfo() {
    val text = buildAnnotatedString {
        append("Earn tickets by participating in Black Friday events. ")
        append(
            AnnotatedString(
                text = "Tap to learn more",
                spanStyle = SpanStyle(
                    textDecoration = TextDecoration.Underline
                )
            )
        )
    }
    Text(
        modifier = Modifier.padding(vertical = PADDING_HEADER_VERTICAL.dp),
        text = text,
        color = Color.White
    )
}

@Composable
private fun TotalEarnedScreen(
    data: List<AchievementTicket>,
    listState: LazyGridState,
    onEndReached: () -> Unit
) {
    Column(
        Modifier.padding(
            horizontal = TotalEarnedTicketsView.LIST_PADDING.dp
        )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(TotalEarnedTicketsView.SPAN_COUNT),
            modifier = Modifier,
            state = listState,
            verticalArrangement = Arrangement.spacedBy(TotalEarnedTicketsView.ITEM_PADDING.dp),
            horizontalArrangement = Arrangement.spacedBy(TotalEarnedTicketsView.ITEM_PADDING.dp)
        ) {
            informationHeader {
                TotalEarnedInfo()
            }
            items(data) { ticket ->
                Box(
                    modifier = Modifier
                        .height(HEIGHT_TICKET.dp)
                        .background(color = Color.Blue)
                )
            }
        }
        if (listState.isScrolledToTheEnd()) {
            onEndReached.invoke()
        }
    }
}
