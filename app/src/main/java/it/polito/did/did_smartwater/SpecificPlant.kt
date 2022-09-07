package it.polito.did.did_smartwater

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.NumberPicker.OnValueChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import app.futured.donut.DonutProgressView
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.polito.did.did_smartwater.ui.main.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


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
        val viewModelRoutesFragment =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        GlobalScope.launch {
            viewModelRoutesFragment.updateViewModel()
        }

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
        val textViewGiorni = view.findViewById<TextView>(R.id.textViewGiorni)
        textViewGiorni.setVisibility(View.GONE)
        val textViewNote = view.findViewById<TextView>(R.id.textViewNote)
        val layoutParams = textViewNote.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, 40, 0, 0)
        val plantNote = view.findViewById<EditText>(R.id.plantNote)
        val donutHumidity = view.findViewById<DonutProgressView>(R.id.donut_view)
        val textHumidity = view.findViewById<TextView>(R.id.textHumidity)

        val sliderHumidity = view.findViewById<Slider>(R.id.seekbarHumidity)
        sliderHumidity.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                db.child("piantaTest").child("humidityThreshold").setValue(sliderHumidity.value)
            }
        })
        sliderHumidity.setVisibility(View.GONE)



        val pickerDays = view.findViewById<NumberPicker>(R.id.pickerDays)
        pickerDays.minValue = 1
        pickerDays.maxValue = 30
        pickerDays.setVisibility(View.GONE)

        //time picker
        val buttonTime = view.findViewById<Button>(R.id.buttonTime)
        buttonTime.setVisibility(View.GONE)
        val pickedTimeText = view.findViewById<TextView>(R.id.pickedTimeText)
        pickedTimeText.setVisibility(View.GONE)
        buttonTime.setOnClickListener(){
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{ timePicker : TimePicker, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                pickedTimeText.text = SimpleDateFormat("HH:mm").format(cal.time)
                db.child("piantaTest").child("startTime").setValue(pickedTimeText.text.toString())
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                Calendar.MINUTE), true).show()
        }

        //lettura humidity dal viewmodel aggiornato a inizio attività
        donutHumidity.cap = 100f
        donutHumidity.addAmount(
            sectionName = "Humidity",
            amount = viewModelRoutesFragment.currentPlant.value!!.humidityLevel,
            color = Color.parseColor("#356CFF") // Optional, pass color if you want to create new section
        )

        //recupero note
        plantNote.setText(viewModelRoutesFragment.currentPlant.value!!.note)
        plantNote.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent -> //triggered when done editing (as clicked done on keyboard)
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                plantNote.clearFocus()
                db.child("piantaTest").child("note").setValue(plantNote.text.toString())
            }
            false
        })


        //recupero info modalità irrigazione
        if(viewModelRoutesFragment.currentPlant.value!!.irrigationMode == 0) {
            buttonManual.isChecked = true
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.VISIBLE)
            sliderHumidity.setVisibility(View.GONE)
            layoutParams.setMargins(0, 40, 0, 0)
            buttonTime.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewGiorni.setVisibility(View.GONE)
        }
        else if (viewModelRoutesFragment.currentPlant.value!!.irrigationMode == 1){
            buttonScheduled.isChecked = true
            calendarView.setVisibility(View.VISIBLE)
            pickerDays.setVisibility(View.VISIBLE)
            textViewGiorni.setVisibility(View.VISIBLE)
            buttonWater.setVisibility(View.GONE)
            sliderHumidity.setVisibility(View.GONE)
            layoutParams.setMargins(0, 1500, 0, 0)
            buttonTime.setVisibility(View.VISIBLE)
            pickedTimeText.setVisibility(View.VISIBLE)

            //recupero data
            val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = formatDate.parse(viewModelRoutesFragment.currentPlant.value!!.startDate)
            calendarView.setDate(date.time)

            //recupero ora
            pickedTimeText.text = viewModelRoutesFragment.currentPlant.value!!.startTime

            //recupero giorni
            pickerDays.value = viewModelRoutesFragment.currentPlant.value!!.irrigationDays
        }
        else if (viewModelRoutesFragment.currentPlant.value!!.irrigationMode == 2){
            buttonAutomatic.isChecked = true
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.GONE)
            sliderHumidity.setVisibility(View.VISIBLE)
            layoutParams.setMargins(0, 300, 0, 0)
            buttonTime.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewGiorni.setVisibility(View.GONE)
        }

        //far leggere da DB la modalità e selezionarla subito
        buttonScheduled.setOnClickListener(){
            calendarView.setVisibility(View.VISIBLE)
            pickerDays.setVisibility(View.VISIBLE)
            textViewGiorni.setVisibility(View.VISIBLE)
            buttonWater.setVisibility(View.GONE)
            db.child("piantaTest").child("irrigationMode").setValue(1)
            GlobalScope.launch {
                viewModelRoutesFragment.updateViewModel()
            }
            sliderHumidity.setVisibility(View.GONE)
            layoutParams.setMargins(0, 1500, 0, 0)
            buttonTime.setVisibility(View.VISIBLE)
            pickedTimeText.setVisibility(View.VISIBLE)
        }

        buttonManual.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.VISIBLE)
            db.child("piantaTest").child("irrigationMode").setValue(0)
            GlobalScope.launch {
                viewModelRoutesFragment.updateViewModel()
            }
            sliderHumidity.setVisibility(View.GONE)
            layoutParams.setMargins(0, 40, 0, 0)
            buttonTime.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewGiorni.setVisibility(View.GONE)
        }

        buttonAutomatic.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.GONE)
            db.child("piantaTest").child("irrigationMode").setValue(2)
            GlobalScope.launch {
                viewModelRoutesFragment.updateViewModel()
            }
            sliderHumidity.setVisibility(View.VISIBLE)
            layoutParams.setMargins(0, 300, 0, 0)
            buttonTime.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewGiorni.setVisibility(View.GONE)
        }

        //aggiornamento data
        calendarView
            .setOnDateChangeListener(
                CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
                    // In this Listener we are getting values
                    // such as year, month and day of month
                    // on below line we are creating a variable
                    // in which we are adding all the variables in it.
                    val date_string = (dayOfMonth.toString() + "-"
                            + (month + 1) + "-" + year)

                    // set this date in TextView for Display
                    db.child("piantaTest").child("startDate").setValue(date_string)
                })

        //aggiornamento giorni irrigazione
        pickerDays.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(numberPicker: NumberPicker, i: Int, i2: Int) {
                db.child("piantaTest").child("irrigationDays").setValue(pickerDays.value)
            }
        })

        buttonWater.setOnClickListener(){
            //scrivere l'avviso di irrigare sul DB
            db.child("ToWater").setValue(1)  //settarlo a false da ESP dopo aver irrigato
            Snackbar
            .make(buttonWater, "Watering your plant...", Snackbar.LENGTH_LONG)
            .setBackgroundTint(0xff00BB2D.toInt())
            .show()
        }



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