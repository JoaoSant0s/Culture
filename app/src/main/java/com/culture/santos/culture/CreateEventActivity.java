package com.culture.santos.culture;

import android.app.DatePickerDialog;
import android.location.Location;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.culture.santos.module.Event;

import java.util.Calendar;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;

    EditText edtName;

    TextView edtLocation;

    EditText edtReferenceLocation;

    Button btnSelectTime;

    EditText edtDescription;

    ImageView edtImage;

    EditText edtExternalLinks;

    Button btnDate;

    private Calendar auxDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        setFields();
        setEvents();

        setDateDialog();
    }

    private void setEvents(){
        btnDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void setFields(){
        edtName = (EditText) findViewById(R.id.edit_name);
        btnDate = (Button) findViewById(R.id.btn_select_time);
        edtLocation =  (TextView) findViewById(R.id.edit_location);
        edtReferenceLocation = (EditText)findViewById(R.id.edit_reference_location);
        btnSelectTime = (Button) findViewById(R.id.btn_select_time);
        edtDescription = (EditText) findViewById(R.id.edit_description);
        edtImage = (ImageView) findViewById(R.id.edit_image);
        edtExternalLinks = (EditText) findViewById(R.id.edit_external_links);
    }

    private void setDateDialog() {
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                auxDate = newDate;
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void createEvent(){
        //Event newEvent = new Event(edtName.getText(), auxDate, new Location(this), edtReferenceLocation.getText(), edtDescription.getText(), edtExternalLinks.getText());


    }
}
