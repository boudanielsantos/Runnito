package com.example.runnito

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.runnito.data.EventDao
import com.example.runnito.model.EventModel


@Database(entities = [EventModel::class], version = 0, exportSchema = false)
abstract class RunnitoDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
}