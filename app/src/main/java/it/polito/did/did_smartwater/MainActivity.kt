package it.polito.did.did_smartwater

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.polito.did.did_smartwater.ui.main.AddPlant
import it.polito.did.did_smartwater.ui.main.MainViewModel
import it.polito.did.did_smartwater.ui.main.Plants

class MainActivity : AppCompatActivity() {

    public var canGoBack = true
    val addPlant = AddPlant()
    val specificPlant = SpecificPlant()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
      /*  if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, Plants.newInstance())
                .commitNow()
        }*/
       //FirebaseApp.getInstance().name

        //val vm = ViewModelProvider(this).get(MainViewModel::class.java)
        //vm.initialize()

       // supportActionBar?.setTitle("SmartWater")
        //supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.barra_fissa_menu))
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        if(canGoBack) {
            if(findNavController(R.id.fragmentContainerView).currentDestination?.id == R.id.plants) {
                Log.d("back", "exit application")
                finishAffinity()
            }
            else
                super.onBackPressed()
                addPlant.setPhotoValue(false)
                specificPlant.setTemporaryScheduled(false)
        }
        else
            finishAffinity()
    }

    public fun setGoBack(value : Boolean) {
        canGoBack = value
    }

}