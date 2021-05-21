package dev.burak.ovapp.model.trips

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fare(
    @SerializedName("priceInCents")
    val priceCents: Int,
    val product: String,
    val travelClass: String,
    val discountType: String
) : Parcelable
