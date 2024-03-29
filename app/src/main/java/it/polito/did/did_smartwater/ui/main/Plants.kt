package it.polito.did.did_smartwater.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import it.polito.did.did_smartwater.MainActivity
import it.polito.did.did_smartwater.R
import it.polito.did.did_smartwater.adapter.ItemAdapter
import it.polito.did.did_smartwater.model.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
          //findNavController().navigate(R.id.action_plants_to_specificPlant)

        }
    }

    public fun onItemClicked(position: Int){
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
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //viewModel.initialize()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBarLoading = view.findViewById<ProgressBar>(R.id.progressBarLoading)
        val imageBackgroundLoading = view.findViewById<ImageView>(R.id.imageBackgroundLoading)
        progressBarLoading.setVisibility(View.VISIBLE)
        imageBackgroundLoading.setVisibility(View.VISIBLE)

        (activity as MainActivity?)?.setGoBack(true)

        imageBackgroundLoading.setOnClickListener(){
            //impedisce di cliccare ciò che è sotto
        }


        //reference viewModel
        //val viewModel by activityViewModels<MainViewModel>()
        //vm = ViewModelProvider(activity).get(MainViewModel::class.java)
        //val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val viewModelRoutesFragment =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        //viewModelRoutesFragment.updateViewModel()

        val buttonAddPlants = view.findViewById<ImageView>(R.id.buttonAddPlants)
        val buttonSettings = view.findViewById<ImageView>(R.id.buttonSettings)
        val buttonProfile = view.findViewById<ImageView>(R.id.buttonProfile)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val cardNoPLants = view.findViewById<ImageView>(R.id.CardNoPlants)
        val textViewNoPlants = view.findViewById<TextView>(R.id.textViewNoPlants)
        val textViewNewPlant = view.findViewById<TextView>(R.id.textViewNewPlant)
        cardNoPLants.setVisibility(View.INVISIBLE)
        textViewNoPlants.setVisibility(View.INVISIBLE)
        textViewNewPlant.setVisibility(View.INVISIBLE)

        GlobalScope.launch {
                if(!viewModelRoutesFragment.viewModelSetted) {
                    progressBarLoading.setVisibility(View.VISIBLE)
                    imageBackgroundLoading.setVisibility(View.VISIBLE)
                    Log.d("Launch", progressBarLoading.isVisible.toString())
                    Log.d("Launch", "strating update viewmodel")
                    //viewModelRoutesFragment.updateViewModel()
                    Log.d("Launch", "finished update")
                    //viewModelRoutesFragment.updatePhoto()

                    Handler(Looper.getMainLooper()).post(Runnable {
                        progressBarLoading.setVisibility(View.VISIBLE)
                        imageBackgroundLoading.setVisibility(View.VISIBLE)
                    })
                }


        }



        val imageViewClick = view.findViewById<ImageView>(R.id.imageViewClick)

        imageViewClick.setOnClickListener(){

            if(findNavController().currentDestination?.id == R.id.plants) {
                if (viewModelRoutesFragment.currentPlant!!.value!!.irrigationMode == -1) {
                    findNavController().navigate(R.id.action_plants_to_addPlant)
                } else {
                    findNavController().navigate(R.id.action_plants_to_specificPlant)
                }
            }
        }

        textViewNewPlant.setOnClickListener {
            if(findNavController().currentDestination?.id == R.id.plants)
                findNavController().navigate(R.id.action_plants_to_addPlant)
        }
        //viewModel + Firebase
        //viewModel.currentPlant.observe(viewLifecycleOwner, Observer {  })
        //viewModel.initialize()




        buttonAddPlants.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.plants)
                findNavController().navigate(R.id.action_plants_to_addPlant)
        }

        buttonSettings.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.plants)
                findNavController().navigate(R.id.action_plants_to_settings)
        }

        buttonProfile.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.plants)
                findNavController().navigate(R.id.action_plants_to_profile)
        }


        //listener che crea la recyclerView
        val nameObserver = Observer<String> { name ->
            // Update the UI, in this case, a TextView.
            recyclerView.adapter = ItemAdapter(requireContext(), viewModelRoutesFragment.plantlist)
            progressBarLoading.setVisibility(View.GONE)
            imageBackgroundLoading.setVisibility(View.GONE)
            if (viewModelRoutesFragment.currentPlant.value!!.irrigationMode == -1){
                recyclerView.setVisibility(View.GONE)
                textViewNoPlants.setVisibility(View.VISIBLE)
                cardNoPLants.setVisibility(View.VISIBLE)
                textViewNewPlant.setVisibility(View.VISIBLE)

            }
        }
        viewModelRoutesFragment.plant_name.observe(viewLifecycleOwner, nameObserver)

        val humidityObserver = Observer<Float> { humidity ->
            // Update the UI, in this case, a TextView.
            recyclerView.adapter = ItemAdapter(requireContext(), viewModelRoutesFragment.plantlist)
        }
        viewModelRoutesFragment.plantHumidityLevel.observe(viewLifecycleOwner, humidityObserver)

    }

}