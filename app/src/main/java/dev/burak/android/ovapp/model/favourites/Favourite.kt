package dev.burak.android.ovapp.model.favourites

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Favourite(
    val from: String,
    val to: String,
    @PrimaryKey(autoGenerate = true) var id: Long? = null
) : Parcelable {
    override fun toString(): String {
        return "$from -> $to"
    }
}
