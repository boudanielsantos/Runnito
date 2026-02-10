package com.example.runnito.repository

import com.example.runnito.data.user.UserDao
import com.example.runnito.model.user.User
import jakarta.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

     fun getFirstUser() = userDao.getFirstUser()

    suspend fun insertUser(user: User) = userDao.insertUser(user)
}


