package it.polito.did.did_smartwater.ui.main

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
    }
}