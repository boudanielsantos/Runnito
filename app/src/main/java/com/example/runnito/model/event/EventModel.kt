package com.example.runnito.model.event

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.runnito.model.Distance
import java.util.Date

@Entity(tableName = "event_model")
data class EventModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val day: String,
    val month: String,
    val year: String,
    var description: String? = null,
    val dateObj: Date?,
    val eventDetailsUrl: String,
    var distanceAvailable: List<Distance>? = null,
    var registrationLink: String? = null,
    var bannerUrl: String? = null,
    var isDetailsPopulated: Boolean = false
)