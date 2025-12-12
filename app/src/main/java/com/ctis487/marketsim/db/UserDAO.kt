package com.ctis487.marketsim.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ctis487.marketsim.model.User
import com.ctis487.marketsim.util.Constants

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM ${Constants.USERTABLE} WHERE uid = :id")
    fun getById(id: Int): User?
}