package com.burak.android.ovapp.model.trips

data class Fare(
    val priceCents: Int,
    val product: String,
    val travelClass: String,
    val discountType: String
)