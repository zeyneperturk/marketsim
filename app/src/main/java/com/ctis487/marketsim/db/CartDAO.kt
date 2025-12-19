package com.ctis487.marketsim.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ctis487.marketsim.model.Cart
import com.ctis487.marketsim.util.Constants

@Dao
interface CartDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(cart: Cart)

    @Query("SELECT * FROM ${Constants.CARTTABLE} WHERE userid = :userId")
    fun getCart(userId: Int): List<Cart>

    @Query("DELETE FROM ${Constants.CARTTABLE} WHERE userid = :userId AND pid = :pid")
    fun remove(userId: Int, pid: Int): Int

    @Query("DELETE FROM ${Constants.CARTTABLE} WHERE userid = :userId")
    fun clear(userId: Int)
}