package it.polito.did.did_smartwater.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception

data class Plant(
    var stringResourceId: Int = 0,
    var name: String ="",
    var irrigationMode: Int = 0,
    var startDate: String = "",
    var startTime: String = "",
    var irrigationDays: Int = 1,
    var humidityLevel: Float = 0f,
    var humidiyThreshold: Int = 0,
    var note: String = ""
    ) {
    companion object{
        /*fun DocumentSnapshot.toPlant(): Plant? {
            try {
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
            catch (e: Exception){
                return null
            }
        }*/
    }
}