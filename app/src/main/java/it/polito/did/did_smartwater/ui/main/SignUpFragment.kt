package it.polito.did.did_smartwater.ui.main

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import it.polito.did.did_smartwater.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var auth: FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //debug da togliere
        val buttonHomeDebug = view.findViewById<Button>(R.id.buttonHomeDebug)
        buttonHomeDebug.setOnClickListener() {
            findNavController().navigate(R.id.action_signUpFragment_to_plants)
        }
        val db = Firebase.database.reference
        val viewModelRoutesFragment =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        val emailText = view.findViewById<EditText>(R.id.userEmail)
        val passwordText = view.findViewById<EditText>(R.id.userPassword)
        val username = view.findViewById<EditText>(R.id.username)
        auth = FirebaseAuth.getInstance()

        var email = ""
        var password = ""

        val buttonSignUp = view.findViewById<Button>(R.id.buttonSignUp)
        buttonSignUp.setOnClickListener(){
            email = emailText.text.toString()
            password = passwordText.text.toString()
            if(email != "" && password.length > 5) {
                activity?.let { it1 ->
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(it1) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignUp", "createUserWithEmail:success")
                                val user = auth.currentUser
                                viewModelRoutesFragment.currentUser = user?.uid.toString()
                                db.child("CurrentUser").setValue(user?.uid.toString())
                                //postNewUser(user?.uid.toString())
                                GlobalScope.launch {
                                    postNewUser(user?.uid.toString(), username.text.toString())
                                    viewModelRoutesFragment.setViewModel()
                                }
                                if(findNavController().currentDestination?.id == R.id.signUpFragment)
                                    findNavController().navigate(R.id.action_signUpFragment_to_plants)

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                                Snackbar
                                    .make(buttonSignUp, "Email o password non validi", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(0xff00BB2D.toInt())
                                    .show()
                            }
                        }
            }

            }

        }

    }

    suspend private fun postNewUser(currentUser: String, username: String) {
        val db = Firebase.database.reference
        db.child(currentUser).child("humidityLevel").setValue(0.1f)
        db.child(currentUser).child("humidityThreshold").setValue(0)
        db.child(currentUser).child("id").setValue(0)
        db.child(currentUser).child("irrigationDays").setValue(0)
        db.child(currentUser).child("irrigationMode").setValue(-1) //per riconoscere che p pianta default
        db.child(currentUser).child("name").setValue("pianta")
        db.child(currentUser).child("note").setValue("note")
        db.child(currentUser).child("startDate").setValue("01-01-1970")
        db.child(currentUser).child("startTime").setValue("00:00")
        db.child(currentUser).child("toWater").setValue(0)
        db.child(currentUser).child("username").setValue(username)
        //upload photo
        val storage = FirebaseStorage.getInstance().getReference()
        val plantRef = storage.child(currentUser)
        val baos = ByteArrayOutputStream()
        val bm = (resources.getDrawable(R.drawable.albero_umidita_con_cerchio) as BitmapDrawable).bitmap
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        plantRef.putBytes(data).await()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}