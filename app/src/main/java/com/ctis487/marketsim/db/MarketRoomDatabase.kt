package com.ctis487.marketsim.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ctis487.marketsim.model.Cart
import com.ctis487.marketsim.model.Product
import com.ctis487.marketsim.model.User

@Database(entities = [Product::class, User::class, Cart::class], version = 1)
abstract class MarketRoomDatabase : RoomDatabase() {
    abstract fun ProductDAO(): ProductDAO
    abstract fun UserDAO(): UserDAO
    abstract fun CartDAO(): CartDAO
}