package it.polito.did.did_smartwater.ui.main

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import it.polito.did.did_smartwater.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddPlant.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddPlant : Fragment(R.layout.fragment_add_plant) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //menu bar references
        val buttonPlants = view.findViewById<ImageView>(R.id.buttonPlants)
        val buttonSettings = view.findViewById<ImageView>(R.id.buttonSettings)
        val buttonProfile = view.findViewById<ImageView>(R.id.buttonProfile)

        buttonPlants.setOnClickListener(){
            findNavController().navigate(R.id.action_addPlant_to_plants)
        }

        buttonSettings.setOnClickListener(){
            findNavController().navigate(R.id.action_addPlant_to_settings)
        }

        buttonProfile.setOnClickListener(){
            findNavController().navigate(R.id.action_addPlant_to_profile)
        }

        //views references
        val textPlantName = view.findViewById<EditText>(R.id.plantName)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val buttonManual = view.findViewById<RadioButton>(R.id.buttonManual)
        val buttonScheduled = view.findViewById<RadioButton>(R.id.buttonScheduled)
        val buttonAutomatic = view.findViewById<RadioButton>(R.id.buttonAutomatic)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val textViewNote = view.findViewById<TextView>(R.id.textViewNote)
        val layoutParams = textViewNote.layoutParams as ViewGroup.MarginLayoutParams
        calendarView.setVisibility(View.GONE)
        layoutParams.setMargins(0, 40, 0, 0)
        val textViewGiorni = view.findViewById<TextView>(R.id.textViewGiorni)
        textViewGiorni.setVisibility(View.GONE)
        val pickerDays = view.findViewById<NumberPicker>(R.id.pickerDays)
        pickerDays.minValue = 1
        pickerDays.maxValue = 30
        pickerDays.setVisibility(View.GONE)
        val textPlantNote = view.findViewById<EditText>(R.id.plantNote)
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        val dateDebug = view.findViewById<TextView>(R.id.dateDebug)    //debug per data select

        //variabili per nuova pianta
        var newPlantName = ""
        var newPlantIrrigation = -1
        var newPlantStartDate = ""
        var newPlantDays = -1
        var newPlantNote = ""

        textPlantName.setOnEditorActionListener(OnEditorActionListener { textView, actionId, keyEvent -> //triggered when done editing (as clicked done on keyboard)
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                textPlantName.clearFocus()
            }
            false
        })

        textPlantNote.setOnEditorActionListener(OnEditorActionListener { textView, actionId, keyEvent -> //triggered when done editing (as clicked done on keyboard)
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                textPlantNote.clearFocus()
            }
            false
        })

        buttonScheduled.setOnClickListener(){
            calendarView.setVisibility(View.VISIBLE)
            layoutParams.setMargins(0, 1500, 0, 0)
            dateDebug.text = "Seleziona data inizio"
            textViewGiorni.setVisibility(View.VISIBLE)
            pickerDays.setVisibility(View.VISIBLE)
        }

        buttonManual.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            layoutParams.setMargins(0, 40, 0, 0)
            textViewGiorni.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
        }

        buttonAutomatic.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            layoutParams.setMargins(0, 40, 0, 0)
            textViewGiorni.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
        }

        calendarView
            .setOnDateChangeListener(
                CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
                    // In this Listener we are getting values
                    // such as year, month and day of month
                    // on below line we are creating a variable
                    // in which we are adding all the cariables in it.
                    val date = (dayOfMonth.toString() + "-"
                            + (month + 1) + "-" + year)

                    // set this date in TextView for Display
                    dateDebug.text = date
                })

        buttonAdd.setOnClickListener(){
            //viewModel.plantsList = viewModel.plantsList.plus("\n").plus(textPlantName.text.toString())
            //prendere nome dalla view
            //prendere radioButton selezionato
            //prendere note dalla view
            newPlantName = textPlantName.text.toString()
            if(radioGroup.checkedRadioButtonId==buttonManual.id)
                newPlantIrrigation = 0
            if(radioGroup.checkedRadioButtonId==buttonScheduled.id){
                newPlantIrrigation = 1
                newPlantDays = pickerDays.value
            }
            if(radioGroup.checkedRadioButtonId==buttonAutomatic.id)
                newPlantIrrigation = 2
            newPlantNote = textPlantNote.text.toString()

            if(newPlantName == ""){
                Snackbar
                    .make(buttonAdd, "Please, choose a name", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xff7f0000.toInt())
                    .show()
            }
            else if(newPlantIrrigation == -1){
                Snackbar
                    .make(buttonAdd, "Please, choose an irrigation mode", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xff7f0000.toInt())
                    .show()
            }
            else if(newPlantIrrigation == 1 && newPlantDays == -1){
                Snackbar
                    .make(buttonAdd, "Please, specify irrigation days", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xff7f0000.toInt())
                    .show()
            }
            else{
                Snackbar
                    .make(buttonAdd, "Plant added succesfully!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xff00BB2D.toInt())
                    .show()
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddPlant.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddPlant().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}