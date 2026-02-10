package com.example.runnito.data.registeredevent

import androidx.room.Embedded
import androidx.room.Relation
import com.example.runnito.model.event.EventModel
import com.example.runnito.model.registeredevent.RegisteredEvent

data class RegisteredEventWithDetails(
    @Embedded
    val registeredEvent: RegisteredEvent,

    @Relation(
        parentColumn = "eventId",
        entityColumn = "id"
    )
    val event: EventModel
)