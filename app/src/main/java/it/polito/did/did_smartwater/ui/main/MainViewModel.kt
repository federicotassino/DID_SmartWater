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
    var currentPlant = MutableLiveData<Plant>()
    var test = 5
    public val plantlist = mutableListOf<Plant>()
    val db = Firebase.database.reference

    //variabili esperimento
    var plant_name = MutableLiveData<String>()
    var plantIrrigationMode =MutableLiveData<Int>()
    var plantStartDate = MutableLiveData<String>()
    var plantStartTime = MutableLiveData<String>()
    var plantIrrigationDays = MutableLiveData<Int>()
    var plantHumidityLevel = MutableLiveData<Float>()
    var plantHumidityThreshold = MutableLiveData<Int>()
    var plantNote = MutableLiveData<String>()



    init {
        viewModelScope.launch {
            currentPlant.value = FirebaseProfileService.getProfileData()
            plantlist.add(currentPlant.value!!)
            Log.d("NomePianta", "Nome della pianta: " + currentPlant.value!!.name)


            //LISTENERS


            val nameListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue()
                    // ...
                    plant_name.value= post.toString()
                    plantlist[0].name = plant_name.value.toString()
                    Log.d("LISTENERS", "nome cambiato a: " + plant_name.value)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            db.child("piantaTest").child("name").addValueEventListener(nameListener)


            val irrigationModeListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue()
                    // ...
                    plantIrrigationMode.value= post.toString().toInt()
                    plantlist[0].irrigationMode = plantIrrigationMode.value!!
                    Log.d("LISTENERS", "modalità cambiata a: " + plantIrrigationMode.value)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            db.child("piantaTest").child("irrigationMode").addValueEventListener(irrigationModeListener)


            val startDateListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue()
                    // ...
                    plantStartDate.value= post.toString()
                    plantlist[0].startDate = plantStartDate.value.toString()
                    Log.d("StartDate",  "Osserva data" + plantStartDate.value.toString())
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            db.child("piantaTest").child("startDate").addValueEventListener(startDateListener)


            val startTimeListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue()
                    // ...
                    plantStartTime.value= post.toString()
                    plantlist[0].startTime = plantStartTime.value.toString()
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            db.child("piantaTest").child("startTime").addValueEventListener(startTimeListener)


            val irrigationDaysListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue()
                    // ...
                    plantIrrigationDays.value= post.toString().toInt()
                    plantlist[0].irrigationDays = plantIrrigationDays.value!!
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            db.child("piantaTest").child("irrigationDays").addValueEventListener(irrigationDaysListener)


            val humidityLevelListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue()
                    // ...
                    plantHumidityLevel.value= post.toString().toFloat()
                    plantlist[0].humidityLevel = plantHumidityLevel.value!!
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            db.child("piantaTest").child("humidityLevel").addValueEventListener(humidityLevelListener)


            val humidityThresholdListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue()
                    // ...
                    plantHumidityThreshold.value= post.toString().toInt()
                    plantlist[0].humidiyThreshold = plantHumidityThreshold.value!!
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            db.child("piantaTest").child("humidityThreshold").addValueEventListener(humidityThresholdListener)


            val noteListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue()
                    // ...
                    plantNote.value= post.toString()
                    plantlist[0].note = plantNote.value!!
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            db.child("piantaTest").child("note").addValueEventListener(noteListener)


            test = FirebaseProfileService.getTest()!!
            Log.d("NomePianta", "Nome della pianta: " + test)
        }
    }

    suspend fun updateViewModel(){
            currentPlant.postValue(FirebaseProfileService.getProfileData())
            plantlist[0].name = plant_name.value.toString()
    }

    fun updateList(){
        plantlist.add(currentPlant.value!!)
    }

}