package it.polito.did.did_smartwater.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val vm by activityViewModels<MainViewModel>()
        //vm.initialize()
    }

    //private val vm by viewModels<MainViewModel>()
    //val vm = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    //private val viewModel by activityViewModels<MainViewModel>()
    //val vm: MainViewModel = ViewModelProvider(this).get(MainViewModel.class)
    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //viewModel.initialize()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //reference viewModel
        //val viewModel by activityViewModels<MainViewModel>()
        //vm = ViewModelProvider(activity).get(MainViewModel::class.java)
        //val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val viewModelRoutesFragment =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


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
        //viewModel.initialize()
        Log.d("viewModel", viewModelRoutesFragment.test.toString())
        val plantObserver = Observer<Plant> {
            newName -> testPlantName.text = newName.name
        }
        //viewModel.currentPlant.observe(viewLifecycleOwner, plantObserver)  //Qua si rompe!!

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