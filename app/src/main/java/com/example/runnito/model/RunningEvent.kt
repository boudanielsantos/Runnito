package com.example.runnito.model

import java.util.Date

data class RunningEvent(
    val title: String,
    val day: String,
    val month: String,
    val year: String,
    val subtitle: String,
    val dateObj: Date?,
    val url: String,
)