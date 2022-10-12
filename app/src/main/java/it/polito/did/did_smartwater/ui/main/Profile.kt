package it.polito.did.did_smartwater.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment(R.layout.fragment_profile) {
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
        val buttonSettings = view.findViewById<ImageView>(R.id.buttonSettings)

        val buttonResetPassword = view.findViewById<Button>(R.id.buttonResetPassword)
        val buttonLogout = view.findViewById<Button>(R.id.buttonLogout)

        val textviewFB = view.findViewById<TextView>(R.id.TV)
        textviewFB.text = Firebase.auth.currentUser?.email.toString()

        val viewModelRoutesFragment =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)



        buttonPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_profile_to_plants)
        }

        buttonAddPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_profile_to_addPlant)
        }

        buttonSettings.setOnClickListener(){
            findNavController().navigate(R.id.action_profile_to_settings)
        }

        var auth = FirebaseAuth.getInstance()
        var email = auth.currentUser?.email.toString()

        buttonResetPassword.setOnClickListener(){
            auth.sendPasswordResetEmail(email)
            Snackbar
                .make(buttonResetPassword, "Ti abbiamo inviato una mail per il reset della password", Snackbar.LENGTH_LONG)
                .setBackgroundTint(0xff00BB2D.toInt())
                .show()
        }



        buttonLogout.setOnClickListener(){
            Firebase.auth.signOut()
            viewModelRoutesFragment.viewModelSetted = false
            findNavController().navigate(R.id.action_profile_to_loginFragment3)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}