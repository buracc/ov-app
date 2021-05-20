package dev.burak.android.ovapp.model.trips

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fare(
    val priceCents: Int,
    val product: String,
    val travelClass: String,
    val discountType: String
) : Parcelable
