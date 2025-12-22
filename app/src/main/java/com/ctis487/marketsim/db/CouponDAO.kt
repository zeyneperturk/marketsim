package com.ctis487.marketsim.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ctis487.marketsim.model.Cart
import com.ctis487.marketsim.model.Coupon
import com.ctis487.marketsim.util.Constants

@Dao
interface CouponDAO {

    @Query("SELECT * FROM ${Constants.COUPONTABLE} ORDER BY cid DESC")
    fun getAllCoupons(): LiveData<List<Coupon>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(coupon: Coupon)

    @Query("SELECT * FROM ${Constants.COUPONTABLE} WHERE cid = :cId")
    fun getCoupon(cId: Int): List<Coupon>

    @Query("DELETE FROM ${Constants.COUPONTABLE} WHERE cid = :cId")
    fun remove(cId: Int): Int
}