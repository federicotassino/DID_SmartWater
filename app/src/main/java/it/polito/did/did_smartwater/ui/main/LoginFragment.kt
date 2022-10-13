package it.polito.did.did_smartwater.ui.main

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
import it.polito.did.did_smartwater.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var auth: FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelRoutesFragment =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        //debug da togliere
        val buttonHomeDebug = view.findViewById<Button>(R.id.buttonHomeDebug)
        buttonHomeDebug.setOnClickListener() {
            GlobalScope.launch {
                viewModelRoutesFragment.setViewModel()
            }
            findNavController().navigate(R.id.action_loginFragment3_to_plants)
        }

        auth = FirebaseAuth.getInstance()


        val buttonToSignUp = view.findViewById<Button>(R.id.buttonToSignUp)
        buttonToSignUp.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.loginFragment3)
                findNavController().navigate(R.id.action_loginFragment3_to_signUpFragment)
        }

        val emailText = view.findViewById<EditText>(R.id.userEmail)
        val passwordText = view.findViewById<EditText>(R.id.userPassword)

        var email = ""
        var password = ""


        val buttonSignIn = view.findViewById<Button>(R.id.buttonSignIn)
        buttonSignIn.setOnClickListener(){
            email = emailText.text.toString()
            password = passwordText.text.toString()
            if(email != "" && password.length > 5) {
                activity?.let { it1 ->
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(it1) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignIn", "signInWithEmail:success")
                                val user = auth.currentUser
                                viewModelRoutesFragment.currentUser = user?.uid.toString()
                                GlobalScope.launch {
                                    viewModelRoutesFragment.setViewModel()
                                }
                                if(findNavController().currentDestination?.id == R.id.loginFragment3)
                                    findNavController().navigate(R.id.action_loginFragment3_to_plants)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignIn", "signInWithEmail:failure", task.exception)
                                Snackbar
                                    .make(buttonSignIn, "Email o password non validi", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(0xff00BB2D.toInt())
                                    .show()
                            }
                        }
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null) {
            val viewModelRoutesFragment =
                ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

            viewModelRoutesFragment.currentUser = currentUser?.uid.toString()
            GlobalScope.launch {
                viewModelRoutesFragment.setViewModel()
            }
            if(findNavController().currentDestination?.id == R.id.loginFragment3)
                findNavController().navigate(R.id.action_loginFragment3_to_plants)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}