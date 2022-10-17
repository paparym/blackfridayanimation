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
import com.practice.blackfridayanimation.available.AvailableTicketsView.Companion.ANIM_FADE_DURATION
import com.practice.blackfridayanimation.available.AvailableTicketsView.Companion.ANIM_PERSISTENCE_DURATION
import com.practice.blackfridayanimation.available.AvailableTicketsView.Companion.HEIGHT_TICKET
import com.practice.blackfridayanimation.available.AvailableTicketsView.Companion.PADDING_HEADER_VERTICAL
import com.practice.blackfridayanimation.available.AvailableTicketsView.Companion.PADDING_LIST_HORIZONTAL
import com.practice.blackfridayanimation.available.AvailableTicketsView.Companion.PADDING_LIST_VERTICAL
import com.practice.blackfridayanimation.available.AvailableTicketsView.Companion.PADDING_TEXT
import com.practice.blackfridayanimation.available.AvailableTicketsView.Companion.SPAN_COUNT
import com.practice.blackfridayanimation.informationHeader
import com.practice.blackfridayanimation.isScrolledToTheEnd
import com.practice.blackfridayanimation.models.AchievementTicket
import com.practice.blackfridayanimation.models.data50
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AvailableTicketsView @JvmOverloads constructor(
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

    companion object {
        internal const val ANIM_FADE_DURATION = 333
        internal const val ANIM_PERSISTENCE_DURATION = 2000L
        internal const val HEIGHT_TICKET = 157
        internal const val PADDING_TEXT = 12
        internal const val PADDING_LIST_HORIZONTAL = 16
        internal const val PADDING_LIST_VERTICAL = 24
        internal const val PADDING_HEADER_VERTICAL = 32
        internal const val SPAN_COUNT = 3
    }
}

@Composable
private fun AvailableInfo() {
    val text = buildAnnotatedString {
        append("Use your available tickets to enter Drops, Raffles and Auctions")
    }
    Text(
        modifier = Modifier.padding(vertical = PADDING_HEADER_VERTICAL.dp),
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
                AvailableTicketsView.TicketAlphaState.FullyVisible
            } else {
                AvailableTicketsView.TicketAlphaState.HalfVisible
            }
        )
    }
    val alpha by remember {
        derivedStateOf {
            when (ticketAlphaState) {
                AvailableTicketsView.TicketAlphaState.FullyVisible -> 1f
                AvailableTicketsView.TicketAlphaState.HalfVisible -> 0.5f
            }
        }
    }
    val animatedAlpha = animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = ANIM_FADE_DURATION, easing = LinearEasing)
    )
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.background(Color.Transparent)
    ) {
        AsyncImage(
            modifier = Modifier
                .height(HEIGHT_TICKET.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (ticketAlphaState == AvailableTicketsView.TicketAlphaState.HalfVisible) {
                        coroutineScope.launch {
                            ticketAlphaState = AvailableTicketsView.TicketAlphaState.FullyVisible
                            delay(ANIM_PERSISTENCE_DURATION)
                            ticketAlphaState = AvailableTicketsView.TicketAlphaState.HalfVisible
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
            modifier = Modifier.padding(top = PADDING_TEXT.dp),
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
            horizontal = PADDING_LIST_HORIZONTAL.dp
        )
    ) {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(SPAN_COUNT),
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(PADDING_LIST_HORIZONTAL.dp),
            verticalArrangement = Arrangement.spacedBy(PADDING_LIST_VERTICAL.dp)

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
