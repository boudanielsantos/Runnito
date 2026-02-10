package com.example.runnito.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.runnito.model.user.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {


    @Query("SELECT * FROM User LIMIT 1")
     fun getFirstUser(): Flow<User?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}