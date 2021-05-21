package dev.burak.ovapp.model.stations

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Station(
    @SerializedName("namen")
    val names: Map<String, String>,
    @SerializedName("land")
    val country: String,
    @SerializedName("UICCode")
    val uicCode: String,
    @SerializedName("synoniemen")
    val otherNames: List<String>
) : Parcelable {
    val name: String
        get() = names["lang"] ?: ""

    override fun toString(): String {
        return name
    }
}
