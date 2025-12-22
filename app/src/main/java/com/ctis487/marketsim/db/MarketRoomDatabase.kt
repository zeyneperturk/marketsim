package com.ctis487.marketsim.db

import android.content.Context
import android.provider.SyncStateContract
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ctis487.marketsim.model.Cart
import com.ctis487.marketsim.model.Coupon
import com.ctis487.marketsim.model.Product
import com.ctis487.marketsim.model.User
import com.ctis487.marketsim.util.Constants

@Database(entities = [Product::class, User::class, Cart::class, Coupon::class], version = 7)
abstract class MarketRoomDatabase : RoomDatabase() {
    abstract fun ProductDAO(): ProductDAO
    abstract fun UserDAO(): UserDAO
    abstract fun CartDAO(): CartDAO
    abstract fun CouponDAO(): CouponDAO

    companion object {
        @Volatile
        private var INSTANCE: MarketRoomDatabase? = null

        fun getDatabase(context: Context): MarketRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarketRoomDatabase::class.java,
                    Constants.DATABASE
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}