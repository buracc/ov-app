package dev.burak.ovapp.exception

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import dev.burak.ovapp.R
import dev.burak.ovapp.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UncaughtExceptionHandler(private val activity: MainActivity) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.e("Uncaught", "Unknown error", e)
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(activity, "Unknown error, please report: ${e.message}", Toast.LENGTH_LONG).show()
            activity.findViewById<ProgressBar>(R.id.searchProgressBar)?.visibility = View.INVISIBLE
        }
    }
}
