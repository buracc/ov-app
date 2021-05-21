package dev.burak.ovapp.exception

import java.lang.Exception

class StationNotFoundException(override val message: String) : Exception(message)
