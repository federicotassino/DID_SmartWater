package it.polito.did.did_smartwater.ui.main

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import it.polito.did.did_smartwater.model.Plant
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.tasks.await

class MainViewModel : ViewModel() {
    //viewModel + Firebase
    //val currentPlant : MutableLiveData<Plant> by lazy { MutableLiveData<Plant>().also { initialize() } }
    var currentPlant = MutableLiveData<Plant>()
    var test = 5
    public val plantlist = mutableListOf<Plant>()
    val db = Firebase.database.reference

    //variabili
    var plant_name = MutableLiveData<String>()
    var plantIrrigationMode =MutableLiveData<Int>()
    var plantStartDate = MutableLiveData<String>()
    var plantStartTime = MutableLiveData<String>()
    var plantIrrigationDays = MutableLiveData<Int>()
    var plantHumidityLevel = MutableLiveData<Float>()
    var plantHumidityThreshold = MutableLiveData<Int>()
    var plantNote = MutableLiveData<String>()
    lateinit var bmp: Bitmap
    //var bmp = MutableLiveData<Bitmap>()

    var viewModelSetted: Boolean = false
    var currentUser = "piantaTest"



    /*init {
        viewModelScope.launch {
            updatePhoto()
            currentPlant.value = FirebaseProfileService.getProfileData(currentUser)
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
            db.child(currentUser).child("name").addValueEventListener(nameListener)


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
            db.child(currentUser).child("irrigationMode").addValueEventListener(irrigationModeListener)


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
            db.child(currentUser).child("startDate").addValueEventListener(startDateListener)


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
            db.child(currentUser).child("startTime").addValueEventListener(startTimeListener)


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
            db.child(currentUser).child("irrigationDays").addValueEventListener(irrigationDaysListener)


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
            db.child(currentUser).child("humidityLevel").addValueEventListener(humidityLevelListener)


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
            db.child(currentUser).child("humidityThreshold").addValueEventListener(humidityThresholdListener)


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
            db.child(currentUser).child("note").addValueEventListener(noteListener)


            //test = FirebaseProfileService.getTest()!!
            //Log.d("NomePianta", "Nome della pianta: " + test)
            /*
            val storage = FirebaseStorage.getInstance().getReference().child("foto")
            val photoBytes = storage.getBytes(10000000).await()
            bmp = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
            */

            viewModelSetted = true
            Log.d("foto", "viewModel init")
        }
    }*/

    suspend fun setViewModel() {
        updatePhoto()

        if (plantlist.size == 0) {
            currentPlant.postValue(FirebaseProfileService.getProfileData(currentUser, this))
            //plantlist.add(currentPlant.value!!)
        } else {
            currentPlant.postValue(FirebaseProfileService.getProfileData(currentUser, this))
            //plantlist[0] = currentPlant.value!!
        }

        //Log.d("NomePianta", "Nome della pianta: " + currentPlant.value!!.name)


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
        db.child(currentUser).child("name").addValueEventListener(nameListener)


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
        db.child(currentUser).child("irrigationMode").addValueEventListener(irrigationModeListener)


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
        db.child(currentUser).child("startDate").addValueEventListener(startDateListener)


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
        db.child(currentUser).child("startTime").addValueEventListener(startTimeListener)


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
        db.child(currentUser).child("irrigationDays").addValueEventListener(irrigationDaysListener)


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
        db.child(currentUser).child("humidityLevel").addValueEventListener(humidityLevelListener)


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
        db.child(currentUser).child("humidityThreshold").addValueEventListener(humidityThresholdListener)


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
        db.child(currentUser).child("note").addValueEventListener(noteListener)


        //test = FirebaseProfileService.getTest()!!
        //Log.d("NomePianta", "Nome della pianta: " + test)
        /*
        val storage = FirebaseStorage.getInstance().getReference().child("foto")
        val photoBytes = storage.getBytes(10000000).await()
        bmp = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
        */

        viewModelSetted = true
        Log.d("foto", "viewModel init")
    }

    suspend fun updatePhoto(){
        //currentPlant.postValue(FirebaseProfileService.getProfileData())
        Log.d("foto", "viewModel updatePhoto 1")
        val storage = FirebaseStorage.getInstance().getReference().child(currentUser)
        val photoBytes = storage.getBytes(100000000).await()
        bmp = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)

        Log.d("foto", "viewModel updatePhoto 2")
        FirebaseProfileService.setBmp(bmp)

        //in teoria non ci entra mai
        if(plantlist.size != 0) {
            plantlist[0].bmp = bmp
            Log.d("foto", "viewModel updatePhoto 3")
        }

    }

    fun updateList(plant: Plant){
        if (plantlist.size == 0) {
            plantlist.add(plant)
        } else {
            plantlist[0] = plant
        }
    }

}