package it.polito.did.did_smartwater.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import it.polito.did.did_smartwater.model.Plant
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.awaitAll
import it.polito.did.did_smartwater.ui.main.MainViewModel
object FirebaseProfileService {
    private const val TAG = "FirebaseProfileService"
    // dichiarazione variabili su cui mettere observer


     suspend fun getProfileData(): Plant? {
        /* con Firestore che non abbiamo
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("piantaTest").document().get().await().toPlant()
        }
        catch (e: Exception){
            Log.e("Error firebase", "Errore firebase", e)
            null
        }*/


        try {
            val db = Firebase.database.reference
            var id = db.child("piantaTest").child("id").get().await().value.toString().toInt()
            var name = db.child("piantaTest").child("name").get().await().value.toString()
            var irrigationMode = db.child("piantaTest").child("irrigationMode").get().await().value.toString().toInt()
            var startDate = db.child("piantaTest").child("startDate").get().await().value.toString()
            var startTime = db.child("piantaTest").child("startTime").get().await().value.toString()
            var irrigationDays = db.child("piantaTest").child("irrigationDays").get().await().value.toString().toInt()
            var humidityLevel = db.child("piantaTest").child("humidityLevel").get().await().value.toString().toFloat()
            var humidityThreshold = db.child("piantaTest").child("humidityThreshold").get().await().value.toString().toInt()
            var note = db.child("piantaTest").child("note").get().await().value.toString()

            return Plant(id, name, irrigationMode, startDate, startTime, irrigationDays, humidityLevel, humidityThreshold, note)
        }
        catch (e: Exception){
            return null
        }
    }

    suspend fun getTest(): Int? {
        val db = Firebase.database.reference
        return db.child("ToWater").get().await().value.toString().toInt()

        /*val ref = db.child("ToWater")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue()
                // ...
                Log.d("TestPianta", post.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)*/
    }
}