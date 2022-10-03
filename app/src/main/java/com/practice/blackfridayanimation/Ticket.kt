package com.practice.blackfridayanimation

import kotlin.random.Random

data class Ticket(val id: Int, val color: Int = Random.nextInt())
