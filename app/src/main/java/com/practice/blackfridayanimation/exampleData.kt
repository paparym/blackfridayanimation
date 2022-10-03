package com.practice.blackfridayanimation

import kotlin.random.Random

val data5 = listOf(
    Ticket(1),
    Ticket(2),
    Ticket(3),
    Ticket(4),
    Ticket(5)
)

val data10 = (data5 + data5).let { list ->
    var id = 0
    list.map { item ->
        item.copy(id = id++, color = Random.nextInt())
    }
}
val data50 = (data10 + data10 + data10 + data10 + data10).let { list ->
    var id = 0
    list.map { item ->
        item.copy(id = id++, color = Random.nextInt())
    }
}
val data100 = (data50 + data50).let { list ->
    var id = 0
    list.map { item ->
        item.copy(id = id++, color = Random.nextInt())
    }
}
