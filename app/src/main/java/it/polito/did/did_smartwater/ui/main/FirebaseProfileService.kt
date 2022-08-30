package it.polito.did.did_smartwater.ui.main

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import it.polito.did.did_smartwater.model.Plant
import it.polito.did.did_smartwater.model.Plant.Companion.toPlant
import kotlinx.coroutines.tasks.await

object FirebaseProfileService {
    private const val TAG = "FirebaseProfileService"
    suspend fun getProfileData(): Plant? {
        val db = FirebaseFirestore.getInstance()
        return db.collection("piantaTest").document().get().await().toPlant()

        //val db = Firebase.database.reference

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