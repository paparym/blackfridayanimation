package com.practice.blackfridayanimation.available

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.practice.blackfridayanimation.R
import com.practice.blackfridayanimation.informationHeader
import com.practice.blackfridayanimation.isScrolledToTheEnd
import com.practice.blackfridayanimation.models.AchievementTicket
import com.practice.blackfridayanimation.models.data50
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AvailableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    @Composable
    override fun Content() {
        var dataFromApi by remember { mutableStateOf(data50) }
        val coroutineScope = rememberCoroutineScope()
        val listState = rememberLazyGridState()
        AvailableTicketsScreen(
            data = dataFromApi,
            coroutineScope = coroutineScope,
            listState = listState,
            onEndReached = {
                dataFromApi = dataFromApi + data50
                Log.d("-->", "Paging worked new size: ${dataFromApi.size}")
            }
        )
    }

    enum class TicketAlphaState {
        FullyVisible,
        HalfVisible
    }
}

@Composable
private fun AvailableInfo() {
    val text = buildAnnotatedString {
        append("Use your available tickets to enter Drops, Raffles and Auctions")
    }
    Text(
        modifier = Modifier.padding(vertical = 32.dp),
        text = text,
        color = Color.White
    )
}

@Composable
private fun TicketItem(
    coroutineScope: CoroutineScope,
    ticket: AchievementTicket
) {
    var ticketAlphaState by remember {
        mutableStateOf(
            if (ticket.status == AchievementTicket.Status.ACTIVE) {
                AvailableView.TicketAlphaState.FullyVisible
            } else {
                AvailableView.TicketAlphaState.HalfVisible
            }
        )
    }
    val alpha by remember {
        derivedStateOf {
            when (ticketAlphaState) {
                AvailableView.TicketAlphaState.FullyVisible -> 1f
                AvailableView.TicketAlphaState.HalfVisible -> 0.5f
            }
        }
    }
    val animatedAlpha = animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 333, easing = LinearEasing)
    )
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.background(Color.Transparent)
    ) {
        AsyncImage(
            modifier = Modifier
                .height(157.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (ticketAlphaState == AvailableView.TicketAlphaState.HalfVisible) {
                        coroutineScope.launch {
                            ticketAlphaState = AvailableView.TicketAlphaState.FullyVisible
                            delay(2000)
                            ticketAlphaState = AvailableView.TicketAlphaState.HalfVisible
                        }
                    }
                },
            contentDescription = ticket.description,
            model = ticket.assetUrl,
            error = painterResource(id = R.drawable.ic_launcher_background),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentScale = ContentScale.FillBounds,
            alpha = animatedAlpha.value
        )
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = "(${ticket.id})",
            color = Color.White
        )
    }
}

@Composable
private fun AvailableTicketsScreen(
    data: List<AchievementTicket>,
    coroutineScope: CoroutineScope,
    listState: LazyGridState,
    onEndReached: () -> Unit
) {
    Column(
        Modifier.padding(
            horizontal = 16.dp
        )
    ) {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(3),
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)

        ) {
            informationHeader {
                AvailableInfo()
            }
            items(data) { ticket ->
                TicketItem(coroutineScope = coroutineScope, ticket = ticket)
            }
            if (listState.isScrolledToTheEnd()) {
                onEndReached.invoke()
            }
        }
    }
}
