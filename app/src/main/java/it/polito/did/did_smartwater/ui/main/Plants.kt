package it.polito.did.did_smartwater.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import it.polito.did.did_smartwater.R

class Plants : Fragment(R.layout.plants) {

    companion object {
        fun newInstance() = Plants()

        fun newInstanceWithBundle(b:Bundle): Plants{
            val f = Plants()
            f.arguments = b
            return f
        }
    }

    private val viewModel by activityViewModels<MainViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val buttonAddPlants = view.findViewById<Button>(R.id.buttonAddPlants)
        val buttonSettings = view.findViewById<Button>(R.id.buttonSettings)
        val buttonProfile = view.findViewById<Button>(R.id.buttonProfile)

        buttonAddPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_plants_to_addPlant)
        }

        buttonSettings.setOnClickListener(){
            findNavController().navigate(R.id.action_plants_to_settings)
        }

        buttonProfile.setOnClickListener(){
            findNavController().navigate(R.id.action_plants_to_profile)
        }

        val plantList = view.findViewById<TextView>(R.id.plantList)
        plantList.text = viewModel.plantsList
    }

}