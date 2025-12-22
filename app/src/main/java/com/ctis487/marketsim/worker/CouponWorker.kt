package com.ctis487.marketsim.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ctis487.marketsim.db.MarketRoomDatabase
import com.ctis487.marketsim.model.Coupon

class CouponWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val score = inputData.getInt("score", 0)

        if (score <= 10) {
            return Result.success()
        }

        val coupon = generateCoupon(score)

        val db = MarketRoomDatabase.getDatabase(applicationContext)
        val couponDAO = db.CouponDAO()

        couponDAO.add(coupon)

        return Result.success()
    }

    private fun generateCoupon(score: Int): Coupon {
        val code = generateRandomCode()
        val discount = score / 10.0

        return Coupon(
            cid = 0,
            code = code,
            discount = discount
        )
    }

    private fun generateRandomCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..10)
            .map { chars.random() }
            .joinToString("")
    }
}
