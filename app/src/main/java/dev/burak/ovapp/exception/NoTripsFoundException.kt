package dev.burak.ovapp.exception

import java.lang.Exception

class NoTripsFoundException(override val message: String) : Exception(message)
