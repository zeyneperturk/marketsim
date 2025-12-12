package com.ctis487.marketsim.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ctis487.marketsim.model.Product
import com.ctis487.marketsim.util.Constants

@Dao
interface ProductDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<Product>)

    @Query("SELECT * FROM ${Constants.PRODUCTTABLE} WHERE pid = :pid")
    fun getById(pid: Int): Product?

    @Query("SELECT * FROM ${Constants.PRODUCTTABLE}")
    fun getAll(): List<Product>
}