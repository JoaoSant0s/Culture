package com.culture.santos.culture

import android.app.Activity
import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import java.util.Calendar

class CreateEventActivity : AppCompatActivity() {

    private var datePickerDialog: DatePickerDialog? = null

    private var edtName: EditText ? = null

    private var edtLocation: TextView? = null

    private var edtReferenceLocation: EditText? = null

    private var btnSelectTime: Button? = null

    private var edtDescription: EditText? = null

    private var edtImage: ImageView? = null

    private var edtExternalLinks: EditText? = null

    private var btnDate: Button? = null

    private var auxDate: Calendar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        setFields()
        setEvents()

        setDateDialog()
    }

    private fun setEvents() {
        btnDate!!.setOnClickListener { datePickerDialog!!.show() }
    }

    private fun setFields() {
        edtName = findViewById<View>(R.id.edit_name) as EditText
        btnDate = findViewById<View>(R.id.btn_select_time) as Button
        edtLocation = findViewById<View>(R.id.edit_location) as TextView
        edtReferenceLocation = findViewById<View>(R.id.edit_reference_location) as EditText
        btnSelectTime = findViewById<View>(R.id.btn_select_time) as Button
        edtDescription = findViewById<View>(R.id.edit_description) as EditText
        edtImage = findViewById<View>(R.id.edit_image) as ImageView
        edtExternalLinks = findViewById<View>(R.id.edit_external_links) as EditText
    }

    private fun setDateDialog() {
        val newCalendar = Calendar.getInstance()

        datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()

            newDate.set(year, monthOfYear, dayOfMonth)
            auxDate = newDate
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
    }

    fun createEvent(view: View) {
        // Event newEvent = new Event(edtName.getText(), auxDate, new Location(this), edtReferenceLocation.getText(), edtDescription.getText(), edtExternalLinks.getText());
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onBackPressed() {
        // When the user hits the back button set the resultCode
        // to Activity.RESULT_CANCELED to indicate a failure
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    companion object {
        //var CURRETEN_MARCKER = "CURRENT_MARKER"
    }
}
