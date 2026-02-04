package com.example.runnito.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "event_model")
data class EventModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val day: String,
    val month: String,
    val year: String,
    val subtitle: String,
    val dateObj: Date?,
    val eventDetailsUrl: String,
    var distanceAvailable: List<Distance>? = null,
    var registrationLink: String? = null,
    var bannerUrl: String? = null,
    var isDetailsPopulated: Boolean = false
)