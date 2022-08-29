package it.polito.did.did_smartwater.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.polito.did.did_smartwater.model.Plant
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    //varibili
    public var humidityTest = 0.5f
    //collegamento a FB
    val db = Firebase.database.reference
    val ref = db.child("humidity")

    public var plantsList = "Orchidea"

    //viewModel + Firebase
    val currentPlant = MutableLiveData<Plant>()

    init {
        viewModelScope.launch {
            currentPlant.value = FirebaseProfileService.getProfileData()
        }
    }
}