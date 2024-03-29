package it.polito.did.did_smartwater.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import it.polito.did.did_smartwater.R
import it.polito.did.did_smartwater.model.Plant
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val REQUEST_CODE = 42
private lateinit var photoFile: File //High quality
private const val FILE_NAME = "photo.jpg" //High quality
private var setPhoto = false
private lateinit var temporaryImage: Bitmap
private var temporaryIrrigationMode = -1
private var temporaryDate = ""
private var temporaryDays = 0
private var temporaryHours = ""
private var temporaryThreshold = "0%"

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


    override fun onResume() {
        super.onResume()
        Log.d("onResume", temporaryIrrigationMode.toString())
        if(temporaryIrrigationMode != -1){
            //setIrrigationMode()
        }
        else {
            val radioGroup = view?.findViewById<RadioGroup>(R.id.radioGroup)
            radioGroup?.clearCheck()

            val textPlantName = view?.findViewById<EditText>(R.id.plantName)
            textPlantName?.setText("")
            val textPlantNote = view?.findViewById<EditText>(R.id.plantNote)
            textPlantNote?.setText("")
        }

        Log.d("onResume", "onResume")
    }

    /*private fun setIrrigationMode() {
        //reimposta la modalità di irrigazione
        val buttonManual = view?.findViewById<RadioButton>(R.id.buttonManual)
        val buttonScheduled = view?.findViewById<RadioButton>(R.id.buttonScheduled)
        val buttonAutomatic = view?.findViewById<RadioButton>(R.id.buttonAutomatic)
        if (temporaryIrrigationMode == 0) {
            buttonManual?.isChecked = true
            buttonScheduled?.isChecked = false
            buttonAutomatic?.isChecked = false
            Log.d("onResume", "setIrrigationMode")
        }
        if (temporaryIrrigationMode == 1) {
            buttonManual?.isChecked = false
            buttonScheduled?.isChecked = true
            buttonAutomatic?.isChecked = false
        }
        if (temporaryIrrigationMode == 2) {
            buttonManual?.isChecked = false
            buttonScheduled?.isChecked = false
            buttonAutomatic?.isChecked = true
        }
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //working viewmodel reference
        val viewModelRoutesFragment =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        var currentUser = viewModelRoutesFragment.currentUser

        //database reference
        val db = Firebase.database.reference



        //menu bar references
        val buttonPlants = view.findViewById<ImageView>(R.id.buttonPlants)
        val buttonSettings = view.findViewById<ImageView>(R.id.buttonSettings)
        val buttonProfile = view.findViewById<ImageView>(R.id.buttonProfile)

        buttonPlants.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.addPlant) {
                setPhotoValue(false)
                findNavController().navigate(R.id.action_addPlant_to_plants)
            }
        }

        buttonSettings.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.addPlant) {
                setPhotoValue(false)
                findNavController().navigate(R.id.action_addPlant_to_settings)
            }
        }

        buttonProfile.setOnClickListener(){
            if(findNavController().currentDestination?.id == R.id.addPlant) {
                setPhotoValue(false)
                findNavController().navigate(R.id.action_addPlant_to_profile)
            }
        }

        //calendar reference
        val cal = Calendar.getInstance()

        //views references
        val textPlantName = view.findViewById<EditText>(R.id.plantName)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val buttonManual = view.findViewById<RadioButton>(R.id.buttonManual)
        val buttonScheduled = view.findViewById<RadioButton>(R.id.buttonScheduled)
        val buttonAutomatic = view.findViewById<RadioButton>(R.id.buttonAutomatic)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val textViewNote = view.findViewById<TextView>(R.id.textViewNote)
        val textViewTheshold = view.findViewById<TextView>(R.id.textViewThreshold)
        textViewTheshold.setVisibility(View.GONE)
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
        val textViewDateSelect = view.findViewById<TextView>(R.id.textViewDateSelect)
        textViewDateSelect.setVisibility(View.GONE)

        //PopUp
        val scrollView2 = view.findViewById<ScrollView>(R.id.scrollView2)
        val imageViewPopUp = view.findViewById<ImageView>(R.id.imageViewPopUp)
        val textViewPopUp = view.findViewById<TextView>(R.id.textViewPopUp)
        val buttonOKPopUp = view.findViewById<Button>(R.id.buttonOKPopUP)
        val buttonAnnullaPopUp = view.findViewById<Button>(R.id.buttonAnnullaPopUp)
        imageViewPopUp.setVisibility(View.GONE)
        textViewPopUp.setVisibility(View.GONE)
        buttonAnnullaPopUp.setVisibility(View.GONE)
        buttonOKPopUp.setVisibility(View.GONE)

        buttonAnnullaPopUp.setOnClickListener() {
            imageViewPopUp.setVisibility(View.GONE)
            textViewPopUp.setVisibility(View.GONE)
            buttonAnnullaPopUp.setVisibility(View.GONE)
            buttonOKPopUp.setVisibility(View.GONE)
            scrollView2.setOnTouchListener(null)
        }



        val textViewThreshold_value = view.findViewById<TextView>(R.id.textViewThreshold_value2)
        textViewThreshold_value.setVisibility(View.GONE)
        val textViewThreshold2 = view.findViewById<TextView>(R.id.textViewThreshold2)
        textViewThreshold2.setVisibility(View.GONE)
        val sliderHumidity = view.findViewById<Slider>(R.id.seekbarHumidity)
        sliderHumidity.setVisibility(View.GONE)
        sliderHumidity.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                textViewThreshold_value.text = sliderHumidity.value.toInt().toString() + "%"
            }
        })

        val buttonCamera = view.findViewById<ImageView>(R.id.buttonCamera)
        val imageViewPhoto = view.findViewById<ImageView>(R.id.imageViewPhoto)

        if(setPhoto)
            applyPhoto(imageViewPhoto, temporaryImage)


        //time picker

        val buttonTime = view.findViewById<Button>(R.id.buttonTime)
        buttonTime.setVisibility(View.GONE)
        var pickedTimeText = view.findViewById<TextView>(R.id.pickedTimeText)
        pickedTimeText.text = SimpleDateFormat("HH:mm").format(cal.time)
        pickedTimeText.setVisibility(View.GONE)
        val textTime = view.findViewById<TextView>(R.id.textTime)
        textTime.setVisibility(View.GONE)


        Log.d("irrigationMode", "valore tempDate " + temporaryDate)
        Log.d("irrigationMode", "valore irrMode " + temporaryIrrigationMode)
        //variabili per nuova pianta
        var newPlantResourceId = ""
        var newPlantName = ""
        var newPlantIrrigationMode = -1
        var newPlantStartDate: String
        if(setPhoto && temporaryDate != "")
            newPlantStartDate = temporaryDate
        else
            newPlantStartDate = SimpleDateFormat("dd-MM-yyyy").format(cal.time)
        var newPlantStartTime = ""
        var newPlantIrrigationDays = -1
        var newPlantHumidityThreshold = 50
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
            layoutParams.setMargins(0, 1900, 0, 0)
            textViewDateSelect.setVisibility(View.VISIBLE)
            textViewGiorni.setVisibility(View.VISIBLE)
            pickerDays.setVisibility(View.VISIBLE)
            buttonTime.setVisibility(View.VISIBLE)
            pickedTimeText.setVisibility(View.VISIBLE)
            sliderHumidity.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.VISIBLE)
            textViewTheshold.setVisibility(View.GONE)
            textViewThreshold_value.setVisibility(View.GONE)
            textViewThreshold2.setVisibility(View.GONE)
            textTime.setVisibility(View.VISIBLE)
            temporaryIrrigationMode = 1
        }

        buttonManual.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            layoutParams.setMargins(0, 40, 0, 0)
            textViewGiorni.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            sliderHumidity.setVisibility(View.GONE)
            buttonTime.setVisibility(View.GONE)
            textViewDateSelect.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewTheshold.setVisibility(View.GONE)
            textViewThreshold_value.setVisibility(View.GONE)
            textViewThreshold2.setVisibility(View.GONE)
            textTime.setVisibility(View.GONE)
            temporaryIrrigationMode = 0
        }

        buttonAutomatic.setOnClickListener(){
            calendarView.setVisibility(View.GONE)
            layoutParams.setMargins(0, 600, 0, 0)
            textViewGiorni.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            sliderHumidity.setVisibility(View.VISIBLE)
            buttonTime.setVisibility(View.GONE)
            textViewDateSelect.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewTheshold.setVisibility(View.VISIBLE)
            textViewThreshold_value.setVisibility(View.VISIBLE)
            textViewThreshold2.setVisibility(View.VISIBLE)
            textTime.setVisibility(View.GONE)
            temporaryIrrigationMode = 2
        }

        //setta tutte le viste dopo aver fatto la foto
        if (temporaryIrrigationMode == 0) {
            Log.d("irrigationMode", "manual")
            buttonManual?.isChecked = true
            buttonScheduled?.isChecked = false
            buttonAutomatic?.isChecked = false
            calendarView.setVisibility(View.GONE)
            layoutParams.setMargins(0, 40, 0, 0)
            textViewGiorni.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            sliderHumidity.setVisibility(View.GONE)
            buttonTime.setVisibility(View.GONE)
            textViewDateSelect.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewTheshold.setVisibility(View.GONE)
            textViewThreshold_value.setVisibility(View.GONE)
            textViewThreshold2.setVisibility(View.GONE)
            textTime.setVisibility(View.GONE)
        }
        if (temporaryIrrigationMode == 1) {
            Log.d("irrigationMode", "scheduled")
            buttonManual?.isChecked = false
            buttonScheduled?.isChecked = true
            buttonAutomatic?.isChecked = false
            calendarView.setVisibility(View.VISIBLE)
            layoutParams.setMargins(0, 1900, 0, 0)
            textViewDateSelect.setVisibility(View.VISIBLE)
            textViewGiorni.setVisibility(View.VISIBLE)
            pickerDays.setVisibility(View.VISIBLE)
            buttonTime.setVisibility(View.VISIBLE)
            pickedTimeText.setVisibility(View.VISIBLE)
            sliderHumidity.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.VISIBLE)
            textViewTheshold.setVisibility(View.GONE)
            textViewThreshold_value.setVisibility(View.GONE)
            textViewThreshold2.setVisibility(View.GONE)
            textTime.setVisibility(View.VISIBLE)

            //setta tutte le info di programmazione
            val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = formatDate.parse(temporaryDate)
            calendarView.setDate(date.time)
            newPlantStartDate = temporaryDate
            pickerDays.value = temporaryDays
            pickedTimeText.text = temporaryHours
        }
        if (temporaryIrrigationMode == 2) {
            Log.d("irrigationMode", "automatic")
            buttonManual?.isChecked = false
            buttonScheduled?.isChecked = false
            buttonAutomatic?.isChecked = true
            calendarView.setVisibility(View.GONE)
            layoutParams.setMargins(0, 600, 0, 0)
            textViewGiorni.setVisibility(View.GONE)
            pickerDays.setVisibility(View.GONE)
            sliderHumidity.setVisibility(View.VISIBLE)
            buttonTime.setVisibility(View.GONE)
            textViewDateSelect.setVisibility(View.GONE)
            pickedTimeText.setVisibility(View.GONE)
            textViewTheshold.setVisibility(View.VISIBLE)
            textViewThreshold_value.setVisibility(View.VISIBLE)
            textViewThreshold2.setVisibility(View.VISIBLE)
            textViewThreshold_value.text = temporaryThreshold
            textTime.setVisibility(View.GONE)
        }



        calendarView
            .setOnDateChangeListener(
                CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
                    // In this Listener we are getting values
                    // such as year, month and day of month
                    // on below line we are creating a variable
                    // in which we are adding all the variables in it.
                    val date = (dayOfMonth.toString() + "-"
                            + (month + 1) + "-" + year)

                    // set this date in TextView for Display
                    newPlantStartDate = date
                })

        //Time picker
        buttonTime.setOnClickListener(){
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker : TimePicker, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                pickedTimeText.text = SimpleDateFormat("HH:mm").format(cal.time)
                newPlantStartTime = pickedTimeText.text.toString()
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        //lancia un intent per aprire la camera
        buttonCamera.setOnClickListener(){
            //per reimpostare la modalità di irrigazione dopo la foto
            if(buttonManual.isChecked)
                temporaryIrrigationMode = 0
            if(buttonScheduled.isChecked) {
                temporaryIrrigationMode = 1
                temporaryDate = newPlantStartDate
                temporaryDays = pickerDays.value
                temporaryHours = pickedTimeText.text.toString()
                Log.d("irrigationMode", temporaryDate)
                Log.d("irrigationMode", temporaryDays.toString())
                Log.d("irrigationMode", temporaryHours)
            }
            if(buttonAutomatic.isChecked) {
                temporaryIrrigationMode = 2
                temporaryThreshold = textViewThreshold_value.text.toString()
            }

            Log.d("irrigationMode", temporaryIrrigationMode.toString())

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)


            //high quality
            photoFile = getPhotoFile(FILE_NAME)
            //val fileProvider = FileProvider.getUriForFile(this, "it.polito.did.did_smartwater.fileprovider", photoFile) //problemi con this quindi -->
            val fileProvider = activity?.let { it1 -> FileProvider.getUriForFile(it1, "it.polito.did.did_smartwater.fileprovider", photoFile) }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            //fine high quality


            startActivityForResult(takePictureIntent, REQUEST_CODE)
        }


        buttonOKPopUp.setOnClickListener() {
            imageViewPopUp.setVisibility(View.GONE)
            textViewPopUp.setVisibility(View.GONE)
            buttonAnnullaPopUp.setVisibility(View.GONE)
            buttonOKPopUp.setVisibility(View.GONE)

            scrollView2.setOnTouchListener(null)

            Snackbar
                .make(buttonAdd, "Pianta aggiunta con successo!", Snackbar.LENGTH_LONG)
                .setBackgroundTint(0xff00BB2D.toInt())
                .show()

            //upload photo
            val storage = FirebaseStorage.getInstance().getReference()
            val plantRef = storage.child(currentUser)
            val baos = ByteArrayOutputStream()
            val bm = (imageViewPhoto.getDrawable() as BitmapDrawable).bitmap
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()
            plantRef.putBytes(data)

            //code to create a new Plant.kt object on the viewmodel
            viewModelRoutesFragment.bmp = bm
            viewModelRoutesFragment.currentPlant.setValue(Plant(1, newPlantName,newPlantIrrigationMode, newPlantStartDate, newPlantStartTime, newPlantIrrigationDays,1f, newPlantHumidityThreshold, newPlantNote, bm))
            viewModelRoutesFragment.plantlist[0] = viewModelRoutesFragment.currentPlant.value!!

            //code to write new plant info on the database
            db.child(currentUser).child("id").setValue(0)
            db.child(currentUser).child("name").setValue(viewModelRoutesFragment.currentPlant.value!!.name)
            db.child(currentUser).child("irrigationMode").setValue((newPlantIrrigationMode))
            db.child(currentUser).child("startDate").setValue(newPlantStartDate)
            db.child(currentUser).child("startTime").setValue(newPlantStartTime)
            db.child(currentUser).child("irrigationDays").setValue(newPlantIrrigationDays)
            db.child(currentUser).child("humidityLevel").setValue(1f)
            db.child(currentUser).child("humidityThreshold").setValue(newPlantHumidityThreshold)
            db.child(currentUser).child("note").setValue(newPlantNote)
        }



        buttonAdd.setOnClickListener(){

            //prendere nome dalla view
            //prendere radioButton selezionato
            //prendere note dalla view
            newPlantName = textPlantName.text.toString()
            if(radioGroup.checkedRadioButtonId==buttonManual.id)
                newPlantIrrigationMode = 0
            if(radioGroup.checkedRadioButtonId==buttonScheduled.id){
                Log.d("startDate", newPlantStartDate)
                //val date = SimpleDateFormat("dd-MM-yyyy").format(calendarView.date)
                //newPlantStartDate = date
                newPlantIrrigationMode = 1
                newPlantIrrigationDays = pickerDays.value
                newPlantStartTime = pickedTimeText.text.toString()
            }
            if(radioGroup.checkedRadioButtonId==buttonAutomatic.id) {
                newPlantIrrigationMode = 2
                newPlantHumidityThreshold = sliderHumidity.value.toInt()
            }
            newPlantNote = textPlantNote.text.toString()

            if(newPlantName == ""){
                Snackbar
                    .make(buttonAdd, "Per favore, scegli un nome per la tua pianta", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xff7f0000.toInt())
                    .show()
            }
            else if(newPlantIrrigationMode == -1){
                Snackbar
                    .make(buttonAdd, "Per favore, seleziona la modalità di irrigazione", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xff7f0000.toInt())
                    .show()
            }
            else if(newPlantIrrigationMode == 1 && newPlantIrrigationDays == -1){
                Snackbar
                    .make(buttonAdd, "Per favore, specifica l'intervallo di irrigazione", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xff7f0000.toInt())
                    .show()
            }

            else{
                imageViewPopUp.setVisibility(View.VISIBLE)
                textViewPopUp.setVisibility(View.VISIBLE)
                buttonAnnullaPopUp.setVisibility(View.VISIBLE)
                buttonOKPopUp.setVisibility(View.VISIBLE)


                scrollView2.setOnTouchListener(OnTouchListener { v, event -> true })
            }
        }
    }

    //High quality
    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        //val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDirectory = getActivity()?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }
    //fine high quality

    //mette la foto nell'imageView
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //val takenImage = data?.extras?.get("data") as Bitmap  //low quality
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)  //high quality
            val imageViewPhoto = view?.findViewById<ImageView>(R.id.imageViewPhoto)
            setPhoto = true
            temporaryImage = takenImage
            if(takenImage.height > takenImage.width) {
                imageViewPhoto?.setImageBitmap(rotateBitmap(takenImage, 90f))  //high quality
                //imageViewPhoto?.setImageBitmap(takenImage)  //low quality}
            }
            else{
                imageViewPhoto?.setImageBitmap(takenImage)  //high quality
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun applyPhoto(imageViewPhoto: ImageView?, takenImage: Bitmap?) {
        Log.d("foto", "applying photo")
        setPhoto = false
        imageViewPhoto?.setImageBitmap(takenImage)
    }


    //la foto a qualità maggiore deve essere ruotata
    fun rotateBitmap(source: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }

    fun setPhotoValue(value: Boolean){
        setPhoto = value
        temporaryIrrigationMode = -1
        temporaryDate = ""
        temporaryDays = 0
        temporaryHours = ""
        val sliderHumidity = view?.findViewById<Slider>(R.id.seekbarHumidity)
        sliderHumidity?.value = 0f
        temporaryThreshold = "0%"
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