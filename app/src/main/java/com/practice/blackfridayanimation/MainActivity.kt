package com.practice.blackfridayanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practice.blackfridayanimation.ui.theme.BlackfridayanimationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val itemList = mutableListOf<Person>()
            repeat(8) {
                itemList.add(Person(firstName = "$it", age = it + 1))
            }

            var personList by remember {
                mutableStateOf(
                    itemList
                )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JustGrid(
    personList: List<Person>
) {
    val configuration = LocalConfiguration.current
    val defaultPadding = 4.dp
    val tabCount = 6
    val itemWidth =
        (configuration.screenWidthDp.dp / tabCount) - (defaultPadding * 2)
    val itemHeight = 82.dp
    LazyColumn {
        items(personList.chunked(6)) { list ->
            val newList = if (list.size == 6) personList else list
            val rowWidth = ((itemWidth * (newList.size)))
            val scrollState = rememberLazyListState()
            LaunchedEffect(Unit) {
                val indexOfLast = list.lastIndexOf(list.last())
                scrollState.scrollToItem(indexOfLast)
//                scrollState.scrollToItem(0)
            }
            LazyRow(
                state = scrollState,
//                modifier = Modifier.requiredWidth(rowWidth),
                userScrollEnabled = false
            ) {
                items(items = newList, key = { it.age }) {
                    val alphaAnimation = remember { Animatable(0f) }
                    var startAnimation by remember { mutableStateOf(false) }
                    val defaultOffset = -(itemWidth * (tabCount + 1) + itemWidth)
                    val offsetX by animateDpAsState(
                        targetValue = if (startAnimation) 0.dp else defaultOffset,
                        animationSpec = tween(
                            durationMillis = 2500,
                            easing = LinearEasing
                        )
                    )
                    LaunchedEffect(Unit) {
                        launch {
                            delay(500)
                            startAnimation = true
                        }
                        launch {
                            alphaAnimation.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = 1000
                                )
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .offset(x = offsetX)
//                            .alpha(alphaAnimation.value)
                            .animateItemPlacement()
                            .background(Color.Gray)
                            .requiredSize(width = itemWidth, height = itemHeight)
                    ) {
                        Text(text = "${it.age}")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun testing() {
    var personList by remember {
        mutableStateOf(
            listOf(
                Person(firstName = "Mark", age = 22),
                Person(firstName = "Mark", age = 23),
                Person(firstName = "Mark", age = 24),
                Person(firstName = "Mark", age = 25),
                Person(firstName = "Mark", age = 26),
                Person(firstName = "Mark", age = 27),
                Person(firstName = "Mark", age = 28)
//                Person(firstName = "Mark", age = 29),
//                Person(firstName = "Mark", age = 30)
            )
        )
    }
    JustGrid(personList = personList)
}

data class Person(val firstName: String, val age: Int)
