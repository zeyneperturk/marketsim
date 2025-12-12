package com.ctis487.marketsim.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ctis487.marketsim.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = Constants.CARTTABLE,
    primaryKeys = ["userid", "pid"]
)
class Cart(
    val userid: Int,
    val pid: Int,
    val quantity: Int
) : Parcelable{
}