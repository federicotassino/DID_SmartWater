package it.polito.did.did_smartwater.ui.main

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import it.polito.did.did_smartwater.model.Plant
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.awaitAll
import it.polito.did.did_smartwater.ui.main.MainViewModel
object FirebaseProfileService {
    private const val TAG = "FirebaseProfileService"
    // dichiarazione variabili su cui mettere observer

    private lateinit var bmp: Bitmap
    private var bmpSetted: Boolean = false
    //var currentUser = ""

     suspend fun getProfileData(currentUser:  String, viewModel: MainViewModel): Plant? {
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
            Log.d("foto", "profile service started")
            val db = Firebase.database.reference
            var id = db.child(currentUser).child("id").get().await().value.toString().toInt()
            var name = db.child(currentUser).child("name").get().await().value.toString()
            var irrigationMode = db.child(currentUser).child("irrigationMode").get().await().value.toString().toInt()
            var startDate = db.child(currentUser).child("startDate").get().await().value.toString()
            var startTime = db.child(currentUser).child("startTime").get().await().value.toString()
            var irrigationDays = db.child(currentUser).child("irrigationDays").get().await().value.toString().toInt()
            var humidityLevel = db.child(currentUser).child("humidityLevel").get().await().value.toString().toFloat()
            var humidityThreshold = db.child(currentUser).child("humidityThreshold").get().await().value.toString().toInt()
            var note = db.child(currentUser).child("note").get().await().value.toString()

            /*
            val storage = FirebaseStorage.getInstance().getReference().child("foto")
            var photoBytes = storage.getBytes(10000000).await()
            var bmp = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
            */

            //in teoria non ci entra mai
            while(!bmpSetted) {
                Log.d("foto", "waiting bmp")
                Thread.sleep(1000)
            }

            Log.d("foto", "profile service finished")
            val plant = Plant(id, name, irrigationMode, startDate, startTime, irrigationDays, humidityLevel, humidityThreshold, note, bmp)
            viewModel.updateList(plant)

            return plant
        }
        catch (e: Exception){
            return null
        }
    }

    fun setBmp(bitmap: Bitmap){
        bmp = bitmap
        bmpSetted = true
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