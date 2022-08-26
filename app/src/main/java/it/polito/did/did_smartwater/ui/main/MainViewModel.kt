package it.polito.did.did_smartwater.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    //varibili
    public var humidityTest = 0.5f
    //collegamento a FB
    val db = Firebase.database.reference
    val ref = db.child("humidity")


    public var plantsList = "Orchidea"
}