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
import kotlinx.coroutines.newCoroutineContext

class MainViewModel : ViewModel() {
    //viewModel + Firebase
    //val currentPlant : MutableLiveData<Plant> by lazy { MutableLiveData<Plant>().also { initialize() } }
    val currentPlant = MutableLiveData<Plant>()
    var test = 5

    init {
        viewModelScope.launch {
            currentPlant.value = FirebaseProfileService.getProfileData()
            //Log.d("NomePianta", "Nome della pianta: " + currentPlant.value!!.name)

            test = FirebaseProfileService.getTest()!!
            Log.d("NomePianta", "Nome della pianta: " + test)
        }
    }

    suspend fun updateViewModel(){
            currentPlant.postValue(FirebaseProfileService.getProfileData())
    }
}