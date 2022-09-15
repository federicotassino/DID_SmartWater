package it.polito.did.did_smartwater.ui.main

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.polito.did.did_smartwater.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Settings.newInstance] factory method to
 * create an instance of this fragment.
 */
class Settings : Fragment(R.layout.fragment_settings) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonPlants = view.findViewById<ImageView>(R.id.buttonPlants)
        val buttonAddPlants = view.findViewById<ImageView>(R.id.buttonAddPlants)
        val buttonProfile = view.findViewById<ImageView>(R.id.buttonProfile)

        buttonPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_settings_to_plants)
        }

        buttonAddPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_settings_to_addPlant)
        }

        buttonProfile.setOnClickListener(){
            findNavController().navigate(R.id.action_settings_to_profile)
        }

        val buttonWifi = view.findViewById<Button>(R.id.buttonWifi)
        val textViewWaterLevel = view.findViewById<TextView>(R.id.textViewWaterLevel)
        val imageViewLevel = view.findViewById<ImageView>(R.id.imageViewLevel)
        val networkName = view.findViewById<TextView>(R.id.networkName)
        val imageProximity = view.findViewById<ImageView>(R.id.imageProximity)
        val imageUmidita = view.findViewById<ImageView>(R.id.imageUmidita)
        val imagePompa = view.findViewById<ImageView>(R.id.imagePompa)

        buttonWifi.setOnClickListener(){
            findNavController().navigate(R.id.action_settings_to_settings_WifiEsp2)
        }
        //references immagini
        //val id_0 = resources.getIdentifier("it.polito.did.did_smartwater:drawable/menu_custom_spento", null, null)
        //val id_1 = resources.getIdentifier("it.polito.did.did_smartwater:drawable/menu_custom_acceso", null, null)

        val db = Firebase.database.reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue()
                // ...
                textViewWaterLevel.text = post.toString()
                //da inserire immagini icona cisterna
                if(textViewWaterLevel.text.toString().toInt() >= 0 && textViewWaterLevel.text.toString().toInt() < 10){
                    //imageViewLevel.setImageResource(id_0)
                    imageViewLevel.setImageResource(R.drawable.menu_custom_spento)
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 10 && textViewWaterLevel.text.toString().toInt() < 20){
                    imageViewLevel.setImageResource(R.drawable.menu_custom_acceso)
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 20 && textViewWaterLevel.text.toString().toInt() < 30){
                    imageViewLevel.setImageResource(R.drawable.x_smart_water)
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 30 && textViewWaterLevel.text.toString().toInt() < 40){
                    imageViewLevel.setImageResource(R.drawable.tic_smart_water)
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 40 && textViewWaterLevel.text.toString().toInt() < 50){
                    imageViewLevel.setImageResource(R.drawable.add_custom_attivo)
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 50 && textViewWaterLevel.text.toString().toInt() < 60){
                    imageViewLevel.setImageResource(R.drawable.add_custom_spento)
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 60 && textViewWaterLevel.text.toString().toInt() < 70){
                    imageViewLevel.setImageResource(R.drawable.auto_mode_accesa)
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 70 && textViewWaterLevel.text.toString().toInt() < 80){
                    imageViewLevel.setImageResource(R.drawable.auto_mode_spento)
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 80 && textViewWaterLevel.text.toString().toInt() < 90){
                    imageViewLevel.setImageResource(R.drawable.ingranaggio_custom_acceso)
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 90 && textViewWaterLevel.text.toString().toInt() <= 100){
                    imageViewLevel.setImageResource(R.drawable.ingranaggio_custom_spento)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.child("waterLevel").addValueEventListener(postListener)


        val networkNameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue()
                // ...
                networkName.text = post.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        db.child("Wifi").child("networkName").addValueEventListener(networkNameListener)


        val proximityListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue()
                // ...
                if(post == true){
                    imageProximity.setImageResource(R.drawable.tic_smart_water)
                }
                if(post == false){
                    imageProximity.setImageResource(R.drawable.x_smart_water)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        db.child("HardwareStatus").child("proximitySensor").addValueEventListener(proximityListener)

        val umiditaListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue()
                // ...
                if(post == true){
                    imageUmidita.setImageResource(R.drawable.tic_smart_water)
                }
                if(post == false){
                    imageUmidita.setImageResource(R.drawable.x_smart_water)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        db.child("HardwareStatus").child("humiditySensor").addValueEventListener(umiditaListener)

        val pompaListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue()
                // ...
                if(post == true){
                    imagePompa.setImageResource(R.drawable.tic_smart_water)
                }
                if(post == false){
                    imagePompa.setImageResource(R.drawable.x_smart_water)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        db.child("HardwareStatus").child("waterPump").addValueEventListener(pompaListener)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Settings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Settings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}