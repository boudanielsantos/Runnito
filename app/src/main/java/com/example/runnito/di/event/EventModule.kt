package com.example.runnito.di.event

import com.example.runnito.RunnitoDatabase
import com.example.runnito.data.event.EventDao
import com.example.runnito.data.registeredevent.RegisteredEventDao
import com.example.runnito.repository.EventRepository
import com.example.runnito.repository.RegisteredEventRepository
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


    @Provides
    @Singleton
    fun provideRegisteredEventDao(database: RunnitoDatabase): RegisteredEventDao =
        database.registeredEventDao()

    @Provides
    @Singleton
    fun provideRegisteredEventRepository(registeredEventDao: RegisteredEventDao): RegisteredEventRepository =
        RegisteredEventRepository(registeredEventDao)

}


