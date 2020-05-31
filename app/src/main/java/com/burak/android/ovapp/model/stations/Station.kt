package com.burak.android.ovapp.model.stations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Station(
    val name: String,
    val country: String,
    val uicCode: String,
    val otherNames: List<String>
) : Parcelable