package it.polito.did.did_smartwater

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import app.futured.donut.DonutProgressView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.polito.did.did_smartwater.ui.main.MainViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SpecificPlant.newInstance] factory method to
 * create an instance of this fragment.
 */
class SpecificPlant : Fragment() {
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

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db= Firebase.database.reference

        //menu bar references
        val buttonPlants = view.findViewById<ImageView>(R.id.buttonPlants)
        val buttonAddPlants = view.findViewById<ImageView>(R.id.buttonAddPlants)
        val buttonSettings = view.findViewById<ImageView>(R.id.buttonSettings)
        val buttonProfile = view.findViewById<ImageView>(R.id.buttonProfile)

        buttonPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_specificPlant_to_plants)
        }

        buttonAddPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_specificPlant_to_addPlant)
        }

        buttonSettings.setOnClickListener(){
            findNavController().navigate(R.id.action_specificPlant_to_settings)
        }

        buttonProfile.setOnClickListener(){
            findNavController().navigate(R.id.action_specificPlant_to_profile)
        }

        //views references
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val buttonManual = view.findViewById<RadioButton>(R.id.buttonManual)
        val buttonScheduled = view.findViewById<RadioButton>(R.id.buttonScheduled)
        val buttonAutomatic = view.findViewById<RadioButton>(R.id.buttonAutomatic)
        val buttonWater = view.findViewById<Button>(R.id.buttonWater)
        buttonWater.setVisibility(View.GONE)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        calendarView.setVisibility(View.GONE)
        val donutHumidity = view.findViewById<DonutProgressView>(R.id.donut_view)
        val textHumidity = view.findViewById<TextView>(R.id.textHumidity)

        var humidityLevel = viewModel.humidityTest
        //inserire codice per spostare le note di conseguenza

        val pickerDays = view.findViewById<NumberPicker>(R.id.pickerDays)
        pickerDays.minValue = 1
        pickerDays.maxValue = 30
        pickerDays.setVisibility(View.GONE)

        //far leggere da DB la modalità e selezionarla subito
        buttonScheduled.setOnClickListener(){
            calendarView.setVisibility(View.VISIBLE)
            pickerDays.setVisibility(View.VISIBLE)
            buttonWater.setVisibility(View.GONE)
            db.child("irrigationMode").setValue(1)
        }

        buttonManual.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.VISIBLE)
            db.child("irrigationMode").setValue(0)

        }

        buttonAutomatic.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.GONE)
            db.child("irrigationMode").setValue(2)
        }

        buttonWater.setOnClickListener(){
            //scrivere l'avviso di irrigare sul DB
            db.child("ToWater").setValue(true)  //settarlo a false da ESP dopo aver irrigato
            Snackbar
            .make(buttonWater, "Watering your plant...", Snackbar.LENGTH_LONG)
            .setBackgroundTint(0xff00BB2D.toInt())
            .show()
        }

        //codice per aggiornare la progressbar
        //da fare: lettura del livello dal DB
        donutHumidity.addAmount("section 1", humidityLevel.toFloat(), color = Color.parseColor("#356CFF"))

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_specific_plant, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SpecificPlant.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SpecificPlant().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}