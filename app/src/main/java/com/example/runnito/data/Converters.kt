package com.example.runnito.data

import androidx.compose.ui.input.key.type
import androidx.room.TypeConverter
import com.example.runnito.model.Distance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {

    @TypeConverter
    fun fromDistanceList(value: List<Distance>?): String? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDistanceList(value: String?): List<Distance>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<Distance>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
