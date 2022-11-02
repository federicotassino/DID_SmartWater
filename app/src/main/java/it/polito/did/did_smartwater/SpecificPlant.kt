package it.polito.did.did_smartwater

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.NumberPicker.OnValueChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import app.futured.donut.DonutProgressView
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.polito.did.did_smartwater.adapter.ItemAdapter
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
    private var date_string = ""
    public var comingBack = false
    private var temporaryScheduled = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db= Firebase.database.reference
        val viewModelRoutesFragment =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val currentUser = viewModelRoutesFragment.currentUser
        val cal = Calendar.getInstance()

        /*GlobalScope.launch {
            viewModelRoutesFragment.updateViewModel()
        }*/

        //menu bar references
        val buttonPlants = view.findViewById<ImageView>(R.id.buttonPlants)
        val buttonAddPlants = view.findViewById<ImageView>(R.id.buttonAddPlants)
        val buttonSettings = view.findViewById<ImageView>(R.id.buttonSettings)
        val buttonProfile = view.findViewById<ImageView>(R.id.buttonProfile)

        buttonPlants.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.specificPlant) {
                setTemporaryScheduled(false)
                findNavController().navigate(R.id.action_specificPlant_to_plants)
            }
        }

        buttonAddPlants.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.specificPlant) {
                setTemporaryScheduled(false)
                findNavController().navigate(R.id.action_specificPlant_to_addPlant)
            }
        }

        buttonSettings.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.specificPlant) {
                setTemporaryScheduled(false)
                findNavController().navigate(R.id.action_specificPlant_to_settings)
            }
        }

        buttonProfile.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.specificPlant) {
                setTemporaryScheduled(false)
                findNavController().navigate(R.id.action_specificPlant_to_profile)
            }
        }

        //views references
        val specificPlantName = view.findViewById<TextView>(R.id.specificPlantName)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val buttonManual = view.findViewById<RadioButton>(R.id.buttonManual)
        val buttonScheduled = view.findViewById<RadioButton>(R.id.buttonScheduled)
        val buttonAutomatic = view.findViewById<RadioButton>(R.id.buttonAutomatic)
        val buttonWater = view.findViewById<Button>(R.id.buttonWater)
        buttonWater.setVisibility(View.GONE)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        //var date_string = SimpleDateFormat("dd-MM-yyyy").format(calendarView.date)
        calendarView.setVisibility(View.GONE)
        val textViewGiorni = view.findViewById<TextView>(R.id.textViewGiorni)
        textViewGiorni.setVisibility(View.GONE)
        val textViewGiorni2 = view.findViewById<TextView>(R.id.textViewGiorni2)
        textViewGiorni2.setVisibility(View.GONE)
        val textViewTime = view.findViewById<TextView>(R.id.textViewTime)
        textViewTime.setVisibility(View.GONE)
        val textViewData = view.findViewById<TextView>(R.id.textViewData)
        textViewData.setVisibility(View.GONE)
        val textViewNote = view.findViewById<TextView>(R.id.textViewNote)
        val layoutParams = textViewNote.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(160, 40, 0, 0)
        val plantNote = view.findViewById<EditText>(R.id.plantNote)
        val donutHumidity = view.findViewById<DonutProgressView>(R.id.donut_view)
        val textHumidity = view.findViewById<TextView>(R.id.textHumidity)
        val textViewTreshold = view.findViewById<TextView>(R.id.textViewTreshold)
        textViewTreshold.setVisibility(View.GONE)
        val buttonSaveScheduled = view.findViewById<Button>(R.id.buttonSaveScheduled)
        buttonSaveScheduled.setVisibility(View.GONE)
        //cards image views to scale
        val card_manual = view.findViewById<ImageView>(R.id.card_manual)
        val card_scheduled = view.findViewById<ImageView>(R.id.card_scheduled)
        val card_auto = view.findViewById<ImageView>(R.id.card_auto)
        val imageViewProfileFoto = view.findViewById<ImageView>(R.id.imageViewProfileFoto)
        imageViewProfileFoto.setImageBitmap(viewModelRoutesFragment.plantlist[0]?.bmp)

        specificPlantName.text = viewModelRoutesFragment.plant_name.value

        val sliderHumidity = view.findViewById<Slider>(R.id.seekbarHumidity)
        sliderHumidity.value = viewModelRoutesFragment.plantHumidityThreshold.value!!.toFloat()
        sliderHumidity.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                db.child(currentUser).child("humidityThreshold").setValue(sliderHumidity.value)
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
        pickedTimeText.text = SimpleDateFormat("HH:mm").format(cal.time)
        pickedTimeText.setVisibility(View.GONE)
        buttonTime.setOnClickListener(){
            val timeSetListener = TimePickerDialog.OnTimeSetListener{ timePicker : TimePicker, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                pickedTimeText.text = SimpleDateFormat("HH:mm").format(cal.time)
                //db.child("piantaTest").child("startTime").setValue(pickedTimeText.text.toString())
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                Calendar.MINUTE), true).show()
        }

        //lettura humidity dal viewmodel aggiornato a view created
        donutHumidity.cap = 100f
        viewModelRoutesFragment.plantHumidityLevel.value?.let {
            donutHumidity.addAmount(
                sectionName = "Humidity",
                amount = it,
                color = Color.parseColor("#356CFF") )
        }

        viewModelRoutesFragment.plantHumidityLevel.observe(viewLifecycleOwner, Observer {
            //your code here
            donutHumidity.cap = 100f
            viewModelRoutesFragment.plantHumidityLevel.value?.let { it1 ->
                donutHumidity.setAmount("Humidity", it1)
            }

        })


        //recupero note
        plantNote.setText(viewModelRoutesFragment.plantNote.value)
        plantNote.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent -> //triggered when done editing (as clicked done on keyboard)
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                plantNote.clearFocus()
                db.child(currentUser).child("note").setValue(plantNote.text.toString())
            }
            false
        })


        //recupero info modalità irrigazione
        if(viewModelRoutesFragment.plantIrrigationMode.value == 0) {
            buttonManual.isChecked = true
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.VISIBLE)
            sliderHumidity.setVisibility(View.GONE)
            layoutParams.setMargins(160, 300, 0, 0)
            buttonTime.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewGiorni.setVisibility(View.GONE)
            textViewGiorni2.setVisibility(View.GONE)
            textViewTime.setVisibility(View.GONE)
            textViewTreshold.setVisibility(View.GONE)
            buttonSaveScheduled.setVisibility(View.GONE)
            textViewData.setVisibility(View.GONE)

            card_manual.scaleX = 1.15f
            card_manual.scaleY = 1.15f
            card_manual.alpha = 1f
            card_scheduled.scaleX = 0.9f
            card_scheduled.scaleY = 0.9f
            card_manual.alpha = 0.9f
            card_auto.scaleX =  0.9f
            card_auto.scaleY = 0.9f
            card_manual.alpha = 0.9f

            buttonManual.scaleX = 0.7f
            buttonManual.scaleY = 0.7f
            buttonScheduled.scaleX = 0.4f
            buttonScheduled.scaleY = 0.4f
            buttonAutomatic.scaleX = 0.4f
            buttonAutomatic.scaleY = 0.4f


        }
        else if (viewModelRoutesFragment.plantIrrigationMode.value == 1){
            buttonScheduled.isChecked = true
            calendarView.setVisibility(View.VISIBLE)
            pickerDays.setVisibility(View.VISIBLE)
            textViewGiorni.setVisibility(View.VISIBLE)
            textViewGiorni2.setVisibility(View.VISIBLE)
            buttonWater.setVisibility(View.GONE)
            sliderHumidity.setVisibility(View.GONE)
            layoutParams.setMargins(160, 2000, 0, 0)
            buttonTime.setVisibility(View.VISIBLE)
            pickedTimeText.setVisibility(View.VISIBLE)
            pickedTimeText.text = SimpleDateFormat("HH:mm").format(cal.time)
            textViewTime.setVisibility(View.VISIBLE)
            textViewTreshold.setVisibility(View.GONE)
            buttonSaveScheduled.setVisibility(View.VISIBLE)
            textViewData.setVisibility(View.VISIBLE)

            card_manual.scaleX = 0.9f
            card_manual.scaleY = 0.9f
            card_manual.alpha = 0.9f
            card_scheduled.scaleX = 1.15f
            card_scheduled.scaleY = 1.15f
            card_scheduled.alpha = 1f
            card_auto.scaleX =  0.9f
            card_auto.scaleY = 0.9f
            card_auto.alpha = 0.9f

            buttonManual.scaleX = 0.4f
            buttonManual.scaleY = 0.4f
            buttonScheduled.scaleX = 0.7f
            buttonScheduled.scaleY = 0.7f
            buttonAutomatic.scaleX = 0.4f
            buttonAutomatic.scaleY = 0.4f

            //recupero data
            val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            viewModelRoutesFragment.plantStartDate.observe(viewLifecycleOwner, Observer {
                //your code here
                val date = formatDate.parse(viewModelRoutesFragment.plantStartDate.value)
                calendarView.setDate(date.time)
            })


            //recupero ora
            pickedTimeText.text = viewModelRoutesFragment.plantStartTime.value

            //recupero giorni
            pickerDays.value = viewModelRoutesFragment.plantIrrigationDays.value!!
        }
        else if (viewModelRoutesFragment.plantIrrigationMode.value == 2){
            buttonAutomatic.isChecked = true
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.GONE)
            sliderHumidity.setVisibility(View.VISIBLE)
            layoutParams.setMargins(160, 450, 0, 0)
            buttonTime.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewGiorni.setVisibility(View.GONE)
            textViewGiorni2.setVisibility(View.GONE)
            textViewTime.setVisibility(View.GONE)
            textViewTreshold.setVisibility(View.VISIBLE)
            buttonSaveScheduled.setVisibility(View.GONE)
            textViewData.setVisibility(View.GONE)

            card_manual.scaleX = 0.9f
            card_manual.scaleY = 0.9f
            card_manual.alpha = 0.9f
            card_scheduled.scaleX = 0.9f
            card_scheduled.scaleY = 0.9f
            card_scheduled.alpha = 0.9f
            card_auto.scaleX =  1.15f
            card_auto.scaleY = 1.15f
            card_auto.alpha = 1f

            buttonManual.scaleX = 0.4f
            buttonManual.scaleY = 0.4f
            buttonScheduled.scaleX = 0.4f
            buttonScheduled.scaleY = 0.4f
            buttonAutomatic.scaleX = 0.7f
            buttonAutomatic.scaleY = 0.7f
        }


        buttonScheduled.setOnClickListener(){
            calendarView.setVisibility(View.VISIBLE)
            pickerDays.setVisibility(View.VISIBLE)
            textViewGiorni.setVisibility(View.VISIBLE)
            textViewGiorni2.setVisibility(View.VISIBLE)
            buttonWater.setVisibility(View.GONE)
            sliderHumidity.setVisibility(View.GONE)
            layoutParams.setMargins(160, 2000, 0, 0)
            buttonTime.setVisibility(View.VISIBLE)
            pickedTimeText.setVisibility(View.VISIBLE)
            pickedTimeText.text = SimpleDateFormat("HH:mm").format(cal.time)
            textViewTime.setVisibility(View.VISIBLE)
            textViewTreshold.setVisibility(View.GONE)
            buttonSaveScheduled.setVisibility(View.VISIBLE)
            textViewData.setVisibility(View.VISIBLE)
            if(viewModelRoutesFragment.plantIrrigationMode.value != 1) {
                viewModelRoutesFragment.plantStartDate.value = SimpleDateFormat("dd-MM-yyyy").format(cal.time)
                val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                val date = formatDate.parse(viewModelRoutesFragment.plantStartDate.value)
                calendarView.setDate(date.time)
                date_string = viewModelRoutesFragment.plantStartDate.value.toString()
            }

            temporaryScheduled = true

            card_manual.scaleX = 0.9f
            card_manual.scaleY = 0.9f
            card_manual.alpha = 0.9f
            card_scheduled.scaleX = 1.15f
            card_scheduled.scaleY = 1.15f
            card_scheduled.alpha = 1f
            card_auto.scaleX =  0.9f
            card_auto.scaleY = 0.9f
            card_auto.alpha = 0.9f

            buttonManual.scaleX = 0.4f
            buttonManual.scaleY = 0.4f
            buttonScheduled.scaleX = 0.7f
            buttonScheduled.scaleY = 0.7f
            buttonAutomatic.scaleX = 0.4f
            buttonAutomatic.scaleY = 0.4f
        }

        buttonManual.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.VISIBLE)
            db.child(currentUser).child("irrigationMode").setValue(0)
            sliderHumidity.setVisibility(View.GONE)
            layoutParams.setMargins(160, 300, 0, 0)
            buttonTime.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewGiorni.setVisibility(View.GONE)
            textViewGiorni2.setVisibility(View.GONE)
            textViewTime.setVisibility(View.GONE)
            textViewTreshold.setVisibility(View.GONE)
            buttonSaveScheduled.setVisibility(View.GONE)
            textViewData.setVisibility(View.GONE)

            temporaryScheduled = false

            card_manual.scaleX =1.15f
            card_manual.scaleY = 1.15f
            card_manual.alpha = 1f
            card_scheduled.scaleX = 0.9f
            card_scheduled.scaleY = 0.9f
            card_scheduled.alpha = 0.9f
            card_auto.scaleX =  0.9f
            card_auto.scaleY = 0.9f
            card_auto.alpha = 0.9f

            buttonManual.scaleX = 0.7f
            buttonManual.scaleY = 0.7f
            buttonScheduled.scaleX = 0.4f
            buttonScheduled.scaleY = 0.4f
            buttonAutomatic.scaleX = 0.4f
            buttonAutomatic.scaleY = 0.4f
        }

        buttonAutomatic.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            buttonWater.setVisibility(View.GONE)
            db.child(currentUser).child("irrigationMode").setValue(2)
            sliderHumidity.setVisibility(View.VISIBLE)
            layoutParams.setMargins(160, 450, 0, 0)
            buttonTime.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewGiorni.setVisibility(View.GONE)
            textViewGiorni2.setVisibility(View.GONE)
            textViewTime.setVisibility(View.GONE)
            textViewTreshold.setVisibility(View.VISIBLE)
            buttonSaveScheduled.setVisibility(View.GONE)
            textViewData.setVisibility(View.GONE)

            temporaryScheduled = false

            card_manual.scaleX = 0.9f
            card_manual.scaleY = 0.9f
            card_manual.alpha = 0.9f
            card_scheduled.scaleX = 0.9f
            card_scheduled.scaleY = 0.9f
            card_scheduled.alpha = 0.9f
            card_auto.scaleX =  1.15f
            card_auto.scaleY = 1.15f
            card_auto.alpha = 1f

            buttonManual.scaleX = 0.4f
            buttonManual.scaleY = 0.4f
            buttonScheduled.scaleX = 0.4f
            buttonScheduled.scaleY = 0.4f
            buttonAutomatic.scaleX = 0.7f
            buttonAutomatic.scaleY = 0.7f
        }

        //aggiornamento data
        calendarView
            .setOnDateChangeListener(
                CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
                    // In this Listener we are getting values
                    // such as year, month and day of month
                    // on below line we are creating a variable
                    // in which we are adding all the variables in it.
                    date_string = (dayOfMonth.toString() + "-"
                            + (month + 1) + "-" + year)


                    //db.child("piantaTest").child("startDate").setValue(date_string)
                })

        //aggiornamento giorni irrigazione
        /*pickerDays.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(numberPicker: NumberPicker, i: Int, i2: Int) {
                db.child("piantaTest").child("irrigationDays").setValue(pickerDays.value)
            }
        })*/

        buttonSaveScheduled.setOnClickListener(){
            //val date = SimpleDateFormat("dd-MM-yyyy").format(calendarView.date)
            //date_string = date
            if(date_string == "")
                date_string = viewModelRoutesFragment.plantStartDate.value.toString()

            db.child(currentUser).child("startDate").setValue(date_string)
            db.child(currentUser).child("irrigationDays").setValue(pickerDays.value)
            db.child(currentUser).child("startTime").setValue(pickedTimeText.text.toString())
            db.child(currentUser).child("irrigationMode").setValue(1)


            val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = formatDate.parse(date_string)
            calendarView.setDate(date.time)


            Snackbar
                .make(buttonSaveScheduled, "Programmazione salvata con successo!", Snackbar.LENGTH_LONG)
                .setBackgroundTint(0xff00BB2D.toInt())
                .show()
        }

        buttonWater.setOnClickListener(){
            //scrivere l'avviso di irrigare sul DB
            db.child(viewModelRoutesFragment.currentUser).child("toWater").setValue(1)  //settarlo a false da ESP dopo aver irrigato
            Snackbar
            .make(buttonWater, "Irrigando la tua pianta...", Snackbar.LENGTH_LONG)
            .setBackgroundTint(0xff00BB2D.toInt())
            .show()
        }



    }

    override fun onResume() {
        super.onResume()
        val viewModelRoutesFragment =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val radioGroup = view?.findViewById<RadioGroup>(R.id.radioGroup)
        val buttonManual = view?.findViewById<RadioButton>(R.id.buttonManual)
        val buttonScheduled = view?.findViewById<RadioButton>(R.id.buttonScheduled)
        val buttonAutomatic = view?.findViewById<RadioButton>(R.id.buttonAutomatic)
        if(!temporaryScheduled) {
            if (viewModelRoutesFragment.plantIrrigationMode.value == 0) {
                buttonManual?.isChecked = true
                buttonScheduled?.isChecked = false
                buttonAutomatic?.isChecked = false
            } else if (viewModelRoutesFragment.plantIrrigationMode.value == 1) {
                buttonManual?.isChecked = false
                buttonScheduled?.isChecked = true
                buttonAutomatic?.isChecked = false
            } else if (viewModelRoutesFragment.plantIrrigationMode.value == 2) {
                buttonManual?.isChecked = false
                buttonScheduled?.isChecked = false
                buttonAutomatic?.isChecked = true
            }
        }
        else {
            buttonManual?.isChecked = false
            buttonScheduled?.isChecked = true
            buttonAutomatic?.isChecked = false
        }
    }

    public fun setTemporaryScheduled(value: Boolean) {
        temporaryScheduled = value
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