package com.culture.santos.culture

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import java.util.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.*
import com.culture.santos.adapter.FirebaseAdapter
import com.culture.santos.module.Event
import com.google.android.gms.maps.model.LatLng

import java.text.SimpleDateFormat

enum class SavedCalendar {
    START, END
}

class EventActivity  : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private val savedStartCalendar = Calendar.getInstance()
    private val savedEndCalendar = Calendar.getInstance()

    //var fbAuth = FirebaseApp.
    var selectedDate = SavedCalendar.START
    var selectedTime = SavedCalendar.START

    private var editTextStartDate: EditText? = null
    private var editTextEndDate: EditText? = null

    private var editTextStartTime: EditText? = null
    private var editTextEndTime: EditText? = null

    private var editName: EditText? = null
    private var editLocalization: EditText? = null
    private var editDescription: EditText? = null
    private var editExternalLinks: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_maps)

        setEventActivityVariable()

        val intent = intent
        editLocalization!!.setText(intent.getStringExtra("EXTRA_MESSAGE"))

        //val textView = findViewById(R.id.text_message) as TextView
        //textView.setText(message);

        var button = findViewById<Button>(R.id.button_create)

        button.setOnClickListener() {
            finishReplay()
        }
    }

    private fun setEventActivityVariable(){
        editTextStartDate = findViewById<EditText>(R.id.text_date_start) as EditText
        editTextStartTime = findViewById<EditText>(R.id.text_time_start) as EditText

        editTextEndDate = findViewById<EditText>(R.id.text_date_end) as EditText
        editTextEndTime = findViewById<EditText>(R.id.text_time_end) as EditText

        editName = findViewById<EditText>(R.id.edit_name) as EditText
        editLocalization = findViewById<EditText>(R.id.edit_localization) as EditText
        editDescription = findViewById<EditText>(R.id.edit_description) as EditText
        editExternalLinks = findViewById<EditText>(R.id.edit_external_links) as EditText

        editTextStartDate!!.setOnClickListener() {
            selectedDate = SavedCalendar.START
            DatePickerDialog(this, this, savedStartCalendar.get(Calendar.YEAR), savedStartCalendar.get(Calendar.MONTH), savedStartCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        editTextStartTime!!.setOnClickListener() {
            selectedTime = SavedCalendar.START
            TimePickerDialog(this, this, savedStartCalendar.get(Calendar.HOUR_OF_DAY), savedStartCalendar.get(Calendar.MINUTE), true).show()
        }

        editTextEndDate!!.setOnClickListener() {
            selectedDate = SavedCalendar.END
            DatePickerDialog(this, this, savedEndCalendar.get(Calendar.YEAR), savedEndCalendar.get(Calendar.MONTH), savedEndCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        editTextEndTime!!.setOnClickListener() {
            selectedTime = SavedCalendar.END
            TimePickerDialog(this, this, savedEndCalendar.get(Calendar.HOUR_OF_DAY), savedEndCalendar.get(Calendar.MINUTE), true).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        if(selectedDate == SavedCalendar.START){
            savedStartCalendar.set(Calendar.YEAR, year)
            savedStartCalendar.set(Calendar.MONTH, monthOfYear)
            savedStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }else{
            savedEndCalendar.set(Calendar.YEAR, year)
            savedEndCalendar.set(Calendar.MONTH, monthOfYear)
            savedEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        updateDateLabel()
    }

    private fun updateDateLabel() {
        val myFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        var time = sdf.format(savedStartCalendar.time)

        if(selectedDate == SavedCalendar.START){
            editTextStartDate!!.setText(time)
        }else{
            time = sdf.format(savedEndCalendar.time)
            editTextEndDate!!.setText(time)
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(selectedTime == SavedCalendar.START){
            savedStartCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            savedStartCalendar.set(Calendar.MINUTE, minute)
        }else{
            savedEndCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            savedEndCalendar.set(Calendar.MINUTE, minute)
        }
        updateTimeLabel()
    }

    private fun updateTimeLabel() {
        val myFormatHour = "HH"
        val myFormatMinutes = "mm"
        val sdfHour = SimpleDateFormat(myFormatHour, Locale.US)
        val sdfMinutes = SimpleDateFormat(myFormatMinutes, Locale.US)

        var finalTime = sdfHour.format(savedStartCalendar.time) + "h:" + sdfMinutes.format(savedStartCalendar.time) + "m"

        if(selectedTime == SavedCalendar.START){
            editTextStartTime!!.setText(finalTime)
        }else{
            finalTime = sdfHour.format(savedEndCalendar.time) + "h:" + sdfMinutes.format(savedEndCalendar.time) + "m"
            editTextEndTime!!.setText(finalTime)
        }
    }

    override fun onStart() {
        super.onStart()
        teste()
    }

    override fun onDestroy(){
        val replyIntent = Intent()
        //replyIntent.putExtra(EXTRA_REPLY, reply)
        setResult(Activity.RESULT_CANCELED, replyIntent)
        finish()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        teste()
    }

    private fun teste(){

    }

    private fun finishReplay(){

        var name = editName!!.text.toString()
        var location = editLocalization!!.text.toString()
        var description = editDescription!!.text.toString()
        var externalLinks = editExternalLinks!!.text.toString()

        if(
                checkInvalidInput(name) ||
                checkInvalidInput(location) ||
                checkInvalidInput(description) ||
                checkInvalidInput(externalLinks) ||

                checkInvalidInput(editTextStartDate!!.text.toString()) ||
                checkInvalidInput(editTextStartTime!!.text.toString()) ||
                checkInvalidInput(editTextEndDate!!.text.toString()) ||
                checkInvalidInput(editTextEndTime!!.text.toString())

                ){
            return invokeToastWrongFeedback("Aviso: Todos os campos são obrigatórios.")
        }else if(invalidDate()){
            return invokeToastWrongFeedback("Aviso: As datas estão confusas, corrija.")
        }

        val intent = intent
        var latLng = intent.getParcelableExtra("EXTRA_MESSAGE_2") as LatLng

        //newLocation : Address, newDescription : String, newExternalLink : String
        var event = Event(name, savedStartCalendar.timeInMillis, savedEndCalendar.timeInMillis, location, description, externalLinks, latLng!!.latitude, latLng!!.longitude)


        FirebaseAdapter.saveEventInUser(event) {
            val replyIntent = Intent()
            replyIntent.putExtra("EXTRA_REPLY",event)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }

    private fun invalidDate(): Boolean{
        return savedStartCalendar.timeInMillis >= savedEndCalendar.timeInMillis;
    }

    private fun invokeToastWrongFeedback(message: String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun checkInvalidInput(message: String): Boolean{
        if(message.trim().length == 0 || message.isBlank() || message.isEmpty()) return true;
        return false;
    }
}