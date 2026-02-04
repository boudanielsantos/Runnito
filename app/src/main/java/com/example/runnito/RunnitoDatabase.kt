package com.example.runnito

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.runnito.data.Converters
import com.example.runnito.data.EventDao
import com.example.runnito.model.EventModel


@Database(entities = [EventModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RunnitoDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
}