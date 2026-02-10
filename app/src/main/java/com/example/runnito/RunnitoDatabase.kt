package com.example.runnito

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.runnito.data.Converters
import com.example.runnito.data.event.EventDao
import com.example.runnito.data.registeredevent.RegisteredEventDao
import com.example.runnito.data.user.UserDao
import com.example.runnito.model.event.EventModel
import com.example.runnito.model.registeredevent.RegisteredEvent
import com.example.runnito.model.user.User


@Database(
    entities = [EventModel::class, RegisteredEvent::class, User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RunnitoDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    abstract fun registeredEventDao(): RegisteredEventDao

    abstract fun userDao(): UserDao
}