package it.polito.did.did_smartwater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.polito.did.did_smartwater.ui.main.MainViewModel
import it.polito.did.did_smartwater.ui.main.Plants

class MainActivity : AppCompatActivity() {


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

        supportActionBar?.setTitle("SmartWater")
        supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.barra_fissa_menu))
        //supportActionBar?.hide()
    }

}