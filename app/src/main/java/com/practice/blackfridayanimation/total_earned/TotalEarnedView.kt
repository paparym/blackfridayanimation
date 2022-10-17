package com.practice.blackfridayanimation.total_earned

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
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
                horizontal = 16.dp
            )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)

            ) {
                header {
                    TotalEarnedInfo()
                }
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
        modifier = Modifier.padding(vertical = 32.dp),
        text = text,
        color = Color.White
    )
//    Text(
//        text = "Earn tickets by participating in Black Friday events. "
//            .plus(
//                ClickableText(
//                    text = AnnotatedString(
//                        text = "Tap to learn more",
//                        spanStyle = SpanStyle(
//                            textDecoration = TextDecoration.Underline,
//                            color = Color.White
//                        )
//                    ),
//                    onClick = {}
//                )
//            ),
//        color = Color.White
//    )
}

private fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}
