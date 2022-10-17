package com.practice.blackfridayanimation.models

import kotlin.random.Random

val data5 = listOf(
    AchievementTicket(
        "1",
        type = AchievementTicket.Type.A,
        description = "",
        status = AchievementTicket.Status.ACTIVE,
        quantity = 0,
        assetUrl = ""
    ),
    AchievementTicket(
        "2",
        type = AchievementTicket.Type.A,
        description = "",
        status = AchievementTicket.Status.INACTIVE,
        quantity = 0,
        assetUrl = ""
    ),
    AchievementTicket(
        "3",
        type = AchievementTicket.Type.A,
        description = "",
        status = AchievementTicket.Status.ACTIVE,
        quantity = 0,
        assetUrl = ""
    ),
    AchievementTicket(
        "4",
        type = AchievementTicket.Type.A,
        description = "",
        status = AchievementTicket.Status.ACTIVE,
        quantity = 0,
        assetUrl = ""
    ),
    AchievementTicket(
        "5",
        type = AchievementTicket.Type.A,
        description = "",
        status = AchievementTicket.Status.ACTIVE,
        quantity = 0,
        assetUrl = ""
    )
)

val data10 = (data5 + data5).let { list ->
    var id = 0
    list.map { item ->
        item.copy(id = id++.toString(), assetUrl = Random.nextInt().toString())
    }
}
val data50 = (data10 + data10 + data10 + data10 + data10).let { list ->
    var id = 0
    list.map { item ->
        item.copy(id = id++.toString(), assetUrl = Random.nextInt().toString())
    }
}
val data100 = (data50 + data50).let { list ->
    var id = 0
    list.map { item ->
        item.copy(id = id++.toString(), assetUrl = Random.nextInt().toString())
    }
}

val data500 = (data100 + data100 + data100 + data100 + data100).let { list ->
    var id = 0
    list.map { item ->
        item.copy(id = id++.toString(), assetUrl = Random.nextInt().toString())
    }
}

val data1000 = (data500 + data500).let { list ->
    var id = 0
    list.map { item ->
        item.copy(id = id++.toString(), assetUrl = Random.nextInt().toString())
    }
}
