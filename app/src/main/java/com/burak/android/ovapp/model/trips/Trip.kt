package com.burak.android.ovapp.model.trips

data class Trip(
    val legs: List<Leg>,
    val fares: List<Fare>
)