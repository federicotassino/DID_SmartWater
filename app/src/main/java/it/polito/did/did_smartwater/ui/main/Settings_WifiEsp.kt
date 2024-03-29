package it.polito.did.did_smartwater.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
 * Use the [Settings_WifiEsp.newInstance] factory method to
 * create an instance of this fragment.
 */
class Settings_WifiEsp : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings__wifi_esp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val db = Firebase.database.reference
        val buttonBack = view.findViewById<Button>(R.id.buttonBack)

        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener(){
            Log.d("CONNECT", "premuto")
            db.child("HardwareStatus").child("modeAccessPoint").setValue(1)
            Snackbar
                .make(button, "Puoi collegarti alla rete del sistema", Snackbar.LENGTH_LONG)
                .setBackgroundTint(0xff00BB2D.toInt())
                .show()
        }

        val networkName2 = view.findViewById<TextView>(R.id.networkName2)

        val networkNameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue()
                // ...
                networkName2.text = post.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        db.child("Wifi").child("networkName").addValueEventListener(networkNameListener)



        buttonBack.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.settings_WifiEsp2)
                findNavController().navigate(R.id.action_settings_WifiEsp2_to_settings)
        }
        //navbar references
        val buttonPlants = view.findViewById<ImageView>(R.id.buttonPlants)
        val buttonAddPlants = view.findViewById<ImageView>(R.id.buttonAddPlants)
        val buttonProfile = view.findViewById<ImageView>(R.id.buttonProfile)
        val buttonSettings = view.findViewById<ImageView>(R.id.buttonSettings)

        buttonPlants.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.settings_WifiEsp2)
                findNavController().navigate(R.id.action_settings_WifiEsp2_to_plants)
        }

        buttonAddPlants.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.settings_WifiEsp2)
                findNavController().navigate(R.id.action_settings_WifiEsp2_to_addPlant)
        }

        buttonProfile.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.settings_WifiEsp2)
                findNavController().navigate(R.id.action_settings_WifiEsp2_to_profile)
        }

        buttonSettings.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.settings_WifiEsp2)
                findNavController().navigate(R.id.action_settings_WifiEsp2_to_settings)
        }



    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Settings_WifiEsp.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Settings_WifiEsp().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}