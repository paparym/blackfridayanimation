package com.practice.blackfridayanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.practice.blackfridayanimation.ui.theme.BlackfridayanimationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var personList by remember {
                mutableStateOf(
                    emptyList<Person>()
                )
            }
            LaunchedEffect(Unit) {
                while (personList.count() < 11) {
                    val newPerson = Person(firstName = "Mark", age = Random.nextInt())
                    val newList = ArrayList(personList)
                    newList.add(0, newPerson)
                    personList = newList
                    delay(1540)
                }
            }
            BlackfridayanimationTheme {
                Column {
                    Box {
                        JustGrid(
                            personList = personList
                        )
                    }
                    Button(
                        onClick = {
                            val newPerson = Person(firstName = "Mark", age = Random.nextInt())
                            val newList = ArrayList(personList)
                            newList.add(0, newPerson)
                            personList = newList
                        }
                    ) {
                        Text(text = "add")
                    }
                }
            }
        }
    }
}

@Composable
fun ComposeGrid() {
    Canvas(modifier = Modifier.fillMaxSize()) {
//        drawRect(
//            color = Color.Blue,
//            topLeft = Offset.,
//            size = 40f
//        )
//        drawArc(
//            color = Color.Green,
//            startAngle = -90f,
//            sweepAngle = 360 * currPercentage,
//            useCenter = false,
//            style = Stroke(
//                width = 5.dp.toPx(),
//                cap = StrokeCap.Round
//            )
//        )
    }
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(Color.Blue)
            .size(width = 54.dp, height = 82.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JustGrid(
    personList: List<Person>
) {
    val configuration = LocalConfiguration.current
    val itemWidth = configuration.screenWidthDp / 6
    val itemHeight = 84.dp
    LazyRow(
        userScrollEnabled = false,
        modifier = Modifier.requiredWidth((10 * itemWidth).dp)
    ) {
        items(items = personList, key = { it.age }) {
            val alphaAnimation = remember { Animatable(0f) }
            val xAnimation = remember { Animatable(-itemWidth - 12f) }
            LaunchedEffect(Unit) {
                launch {
                    xAnimation.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = 1500,
                            easing = LinearEasing
                        )
                    )
                }
                launch {
                    alphaAnimation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearEasing
                        )
                    )
                }
            }
            val index = personList.indexOf(it)
            Box(
                modifier = Modifier
                    .offset(x = xAnimation.value.dp)
//                        .alpha(alphaAnimation.value)
                    .padding(4.dp)
                    .animateItemPlacement(
                        animationSpec = if (index == 5) {
                            snap()
                        } else {
                            tween(
                                durationMillis = 1500,
                                easing = LinearEasing
                            )
                        }
                    )
                    .background(Color.Blue)
                    .size(width = itemWidth.dp, height = itemHeight)
            ) {
                Text(text = "${it.firstName} ${it.age}")
            }
        }
    }
}

data class Person(val firstName: String, val age: Int)
