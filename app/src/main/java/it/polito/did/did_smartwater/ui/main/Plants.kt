package it.polito.did.did_smartwater.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import it.polito.did.did_smartwater.R
import it.polito.did.did_smartwater.adapter.ItemAdapter
import it.polito.did.did_smartwater.data.DataSource
import it.polito.did.did_smartwater.model.Plant

class Plants : Fragment(R.layout.plants) {

    companion object {
        fun newInstance() = Plants()

        fun newInstanceWithBundle(b:Bundle): Plants{
            val f = Plants()
            f.arguments = b
            return f
        }

        fun onItemClickedCompanion() {
           //can't call findNavController
        }
    }

    fun onItemClicked(position: Int){
        findNavController().navigate(R.id.action_plants_to_specificPlant)
    }

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val buttonAddPlants = view.findViewById<ImageView>(R.id.buttonAddPlants)
        val buttonSettings = view.findViewById<ImageView>(R.id.buttonSettings)
        val buttonProfile = view.findViewById<ImageView>(R.id.buttonProfile)

        val plantList = view.findViewById<TextView>(R.id.plantList)
        val plantListSize = view.findViewById<TextView>(R.id.textViewListSize)
        val testPlantName = view.findViewById<TextView>(R.id.testPlantName)
        val myDataSet = DataSource().loadPlants()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val buttonNavigateToSpecificPlants = view.findViewById<TextView>(R.id.TospecificPlants)

        //viewModel + Firebase
        //viewModel.currentPlant.observe(viewLifecycleOwner, Observer {  })
        val plantObserver = Observer<Plant> {
            newName -> testPlantName.text = newName.name
        }
        //viewModel.currentPlant.observe(viewLifecycleOwner, plantObserver)  Qua si rompe!!

        //recyclerView.addOnItemTouchListener(new RecyclerItemClickListener)

        buttonAddPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_plants_to_addPlant)
        }

        buttonSettings.setOnClickListener(){
            findNavController().navigate(R.id.action_plants_to_settings)
        }

        buttonProfile.setOnClickListener(){
            findNavController().navigate(R.id.action_plants_to_profile)
        }

        //per accedere a specific plant DA RIVEDERE
        buttonNavigateToSpecificPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_plants_to_specificPlant)
        }

        plantList.text = myDataSet[0].name

        //debug dimensione lista (da togliere)
        plantListSize.text = DataSource().loadPlants().size.toString()

        recyclerView.adapter = ItemAdapter(requireContext(), myDataSet)
    }

}