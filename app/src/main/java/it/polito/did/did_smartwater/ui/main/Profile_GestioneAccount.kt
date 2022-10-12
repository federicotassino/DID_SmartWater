package it.polito.did.did_smartwater.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import it.polito.did.did_smartwater.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile_GestioneAccountù.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile_GestioneAccountù : Fragment() {
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
        return inflater.inflate(R.layout.fragment_profile__gestione_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //navigation bar
        val buttonPlants = view.findViewById<ImageView>(R.id.buttonPlants)
        val buttonAddPlants = view.findViewById<ImageView>(R.id.buttonAddPlants)
        val buttonSettings = view.findViewById<ImageView>(R.id.buttonSettings)

        buttonPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_profile_to_plants)
        }

        buttonAddPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_profile_to_addPlant)
        }

        buttonSettings.setOnClickListener(){
            findNavController().navigate(R.id.action_profile_to_settings)
        }


        val buttonUpdatePassword = view.findViewById<Button>(R.id.buttonUpdatePassword)

        var auth = FirebaseAuth.getInstance()
        var old_password = ""
        var new_password_1 = ""
        var new_password_2 = ""
        var correct_old_password = ""
        //val user = FirebaseAuth.getInstance().currentUser


        buttonUpdatePassword.setOnClickListener(){
            //tutto da mettere n if password corrente è giusta
            if (new_password_1 == new_password_2 && new_password_1.length>5 && new_password_2.length>5){
                //aggiornamento e autenticazione
            }
            else if(new_password_1 != new_password_2)
            {
                Snackbar
                    .make(buttonUpdatePassword, "Le due password non corrispondono", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xff7f0000.toInt())
                    .show()
            }
            else if(new_password_1.length<5){
                Snackbar
                    .make(buttonUpdatePassword, "Password troppo corta", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xff7f0000.toInt())
                    .show()
            }
        }



    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile_GestioneAccountù.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile_GestioneAccountù().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}