package com.ctis487.marketsim.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ctis487.marketsim.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constants.USERTABLE)
class User(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    var username: String,
    var firstName: String,
    var lastName: String
): Parcelable {
}