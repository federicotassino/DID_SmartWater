package it.polito.did.did_smartwater.model

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot

data class Plant(
    val stringResourceId: Int,
    val name: String,
    val irrigationMode: Int,
    val startDate: String,
    val irrigationDays: Int,
    val humidityLevel: Float,
    val note: String
    ) {
    companion object{
        fun DocumentSnapshot.toPlant(): Plant? {
            val stringResourceId = getString("id")!!.toInt()
            val name = getString("name")!!
            val irrigationMode = getString("irrigationMode")!!.toInt()
            val startDate = getString("startDate")!!
            val irrigationDays = getString("irrigationDays")!!.toInt()
            val humidityLevel = getString("humidityLevel")!!.toFloat()
            val note = getString("note")!!
            Log.d(null, name)
            return Plant(stringResourceId, name, irrigationMode, startDate, irrigationDays, humidityLevel, note)
        }
    }
}