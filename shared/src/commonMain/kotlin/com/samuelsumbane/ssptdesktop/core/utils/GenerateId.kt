package com.samuelsumbane.ssptdesktop.core.utils

import kotlin.random.Random

fun generateId(length: Int = 2): Int = Random.nextInt().toString().take(length).toInt()