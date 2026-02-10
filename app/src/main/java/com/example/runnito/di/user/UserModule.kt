package com.example.runnito.di.user

import com.example.runnito.RunnitoDatabase
import com.example.runnito.data.user.UserDao
import com.example.runnito.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserDao(database: RunnitoDatabase): UserDao =
        database.userDao()

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository =
        UserRepository(userDao)
}