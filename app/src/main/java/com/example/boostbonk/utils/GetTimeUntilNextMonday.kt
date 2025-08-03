package com.example.boostbonk.utils

import java.util.Calendar
import java.util.concurrent.TimeUnit

fun getTimeUntilNextMonday(): String {
    val now = Calendar.getInstance()
    val nextMonday = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        add(Calendar.WEEK_OF_YEAR, if (now.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && before(now)) 1 else 0)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (before(now)) add(Calendar.WEEK_OF_YEAR, 1)
    }

    val diffMillis = nextMonday.timeInMillis - now.timeInMillis

    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(diffMillis) % 60

    return "${days}d ${hours}h ${minutes}m ${seconds}s"
}