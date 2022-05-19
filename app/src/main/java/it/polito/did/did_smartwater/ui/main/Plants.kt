package it.polito.did.did_smartwater.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import it.polito.did.did_smartwater.R
import it.polito.did.did_smartwater.adapter.ItemAdapter
import it.polito.did.did_smartwater.data.DataSource

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
        findNavController().navigate(R.id.action_plants_to_plants_SpecificPlant)
    }

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val buttonAddPlants = view.findViewById<Button>(R.id.buttonAddPlants)
        val buttonSettings = view.findViewById<Button>(R.id.buttonSettings)
        val buttonProfile = view.findViewById<Button>(R.id.buttonProfile)
        val plantList = view.findViewById<TextView>(R.id.plantList)
        val plantListSize = view.findViewById<TextView>(R.id.textViewListSize)
        val myDataSet = DataSource().loadPlants()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

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


        plantList.text = myDataSet[0].name

        //debug dimensione lista (da togliere)
        plantListSize.text = DataSource().loadPlants().size.toString()

        recyclerView.adapter = ItemAdapter(requireContext(), myDataSet)
    }

}