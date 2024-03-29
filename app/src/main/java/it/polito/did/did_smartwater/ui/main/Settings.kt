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
            if(findNavController().currentDestination?.id == R.id.settings)
                findNavController().navigate(R.id.action_settings_to_plants)
        }

        buttonAddPlants.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.settings)
                findNavController().navigate(R.id.action_settings_to_addPlant)
        }

        buttonProfile.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.settings)
                findNavController().navigate(R.id.action_settings_to_profile)
        }

        val buttonWifi = view.findViewById<Button>(R.id.buttonWifi)
        val textViewWaterLevel = view.findViewById<TextView>(R.id.textViewWaterLevel)
        val imageViewLevel = view.findViewById<ImageView>(R.id.imageViewLevel)
        val networkName = view.findViewById<TextView>(R.id.networkName)
        val imageProximity = view.findViewById<ImageView>(R.id.imageProximity)
        val imageUmidita = view.findViewById<ImageView>(R.id.imageUmidita)
        val imagePompa = view.findViewById<ImageView>(R.id.imagePompa)
        val textViewRicarica = view.findViewById<TextView>(R.id.textViewRicarica)
        textViewRicarica.visibility = View.INVISIBLE
        val textViewProximityError = view.findViewById<TextView>(R.id.textViewProximityError)
        textViewProximityError.visibility = View.INVISIBLE

        buttonWifi.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.settings)
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
                Log.d("TEXT", textViewWaterLevel.text.toString())
                //da inserire immagini icona cisterna
                if(textViewWaterLevel.text.toString().toInt() >= 0 && textViewWaterLevel.text.toString().toInt() < 5){
                    //imageViewLevel.setImageResource(id_0)
                    imageViewLevel.setImageResource(R.drawable.cisterna_0)
                    textViewRicarica.visibility = View.VISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 5 && textViewWaterLevel.text.toString().toInt() < 15){
                    imageViewLevel.setImageResource(R.drawable.cisterna_10)
                    textViewRicarica.visibility = View.VISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 15 && textViewWaterLevel.text.toString().toInt() < 25){
                    imageViewLevel.setImageResource(R.drawable.cisterna_20)
                    textViewRicarica.visibility = View.INVISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 25 && textViewWaterLevel.text.toString().toInt() < 35){
                    imageViewLevel.setImageResource(R.drawable.cisterna_30)
                    textViewRicarica.visibility = View.INVISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 35 && textViewWaterLevel.text.toString().toInt() < 45){
                    imageViewLevel.setImageResource(R.drawable.cisterna_40)
                    textViewRicarica.visibility = View.INVISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 45 && textViewWaterLevel.text.toString().toInt() < 55){
                    imageViewLevel.setImageResource(R.drawable.cisterna_50)
                    textViewRicarica.visibility = View.INVISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 55 && textViewWaterLevel.text.toString().toInt() < 65){
                    imageViewLevel.setImageResource(R.drawable.cisterna_60)
                    textViewRicarica.visibility = View.INVISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 65 && textViewWaterLevel.text.toString().toInt() < 75){
                    imageViewLevel.setImageResource(R.drawable.cisterna_70)
                    textViewRicarica.visibility = View.INVISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 75 && textViewWaterLevel.text.toString().toInt() < 85){
                    imageViewLevel.setImageResource(R.drawable.cisterna_80)
                    textViewRicarica.visibility = View.INVISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 85 && textViewWaterLevel.text.toString().toInt() < 95){
                    imageViewLevel.setImageResource(R.drawable.cisterna_90)
                    textViewRicarica.visibility = View.INVISIBLE
                }
                else if(textViewWaterLevel.text.toString().toInt() >= 95 && textViewWaterLevel.text.toString().toInt() <= 100){
                    imageViewLevel.setImageResource(R.drawable.cisterna_100)
                    textViewRicarica.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.child("HardwareStatus").child("waterLevel").addValueEventListener(postListener)


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
                val post = dataSnapshot.getValue().toString()
                // ...
                if(post == "1"){
                    imageProximity.setImageResource(R.drawable.tic_smart_water)
                    textViewProximityError.visibility = View.INVISIBLE
                }
                if(post == "0"){
                    imageProximity.setImageResource(R.drawable.x_smart_water)
                    textViewProximityError.visibility = View.VISIBLE
                    textViewRicarica.visibility= View.INVISIBLE
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
                val post = dataSnapshot.getValue().toString()
                // ...
                if(post == "1"){
                    imageUmidita.setImageResource(R.drawable.tic_smart_water)
                }
                if(post == "0"){
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
                val post = dataSnapshot.getValue().toString()
                Log.d("POST", post)
                // ...
                if(post == "1"){
                    imagePompa.setImageResource(R.drawable.tic_smart_water)
                }
                if(post == "0"){
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