package com.ctis487.marketsim.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ctis487.marketsim.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constants.PRODUCTTABLE)
class Product(
    @PrimaryKey
    val pid: Int,
    val name: String,
    val price: Double,
    val img: String,
    val description: String
): Parcelable {
}