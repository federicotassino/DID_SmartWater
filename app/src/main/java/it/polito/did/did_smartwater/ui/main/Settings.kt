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

        buttonWifi.setOnClickListener(){
            findNavController().navigate(R.id.action_settings_to_settings_WifiEsp2)
        }
        //references immagini
        val id_0 = resources.getIdentifier("it.polito.did.did_smartwater:drawable/menu_custom_spento", null, null)
        val id_1 = resources.getIdentifier("it.polito.did.did_smartwater:drawable/menu_custom_acceso", null, null)

        val db = Firebase.database.reference
        val refLevel = db.child("waterLevel")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue()
                // ...
                textViewWaterLevel.text = post.toString()
                //da inserire immagini icona cisterna
                if(textViewWaterLevel.text == "0"){
                    imageViewLevel.setImageResource(id_0)
                }
                if(textViewWaterLevel.text == "1"){
                    imageViewLevel.setImageResource(id_1)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        refLevel.addValueEventListener(postListener)


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