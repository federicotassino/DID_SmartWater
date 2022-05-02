package it.polito.did.did_smartwater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}