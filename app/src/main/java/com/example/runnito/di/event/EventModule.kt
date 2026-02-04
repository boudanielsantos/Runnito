package com.example.runnito.di.event

import com.example.runnito.RunnitoDatabase
import com.example.runnito.data.EventDao
import com.example.runnito.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventModule {

    @Provides
    @Singleton
    fun provideEventDao(database: RunnitoDatabase): EventDao =
        database.eventDao()

    @Provides
    @Singleton
    fun provideEventRepository(eventDao: EventDao): EventRepository =
        EventRepository(eventDao)

}


