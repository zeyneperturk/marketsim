package com.ctis487.marketsim.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ctis487.marketsim.util.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constants.PRODUCTTABLE)
class Product(
    @PrimaryKey
    @SerializedName("pid")
    val pid: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("img")
    val img: String,
    @SerializedName("description")
    val description: String
): Parcelable {
}