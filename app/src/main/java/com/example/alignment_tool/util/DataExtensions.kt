package com.example.alignment_tool.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toReadableDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return formatter.format(Date(this))
}
