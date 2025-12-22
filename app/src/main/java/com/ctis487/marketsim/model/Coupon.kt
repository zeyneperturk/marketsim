package com.ctis487.marketsim.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ctis487.marketsim.util.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constants.COUPONTABLE)
class Coupon (
    @PrimaryKey(autoGenerate = true)
    val cid: Int,
    val code: String,
    val discount: Double
) : Parcelable {

}