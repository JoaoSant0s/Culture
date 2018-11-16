package com.culture.santos.culture

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.widget.Button

class EventActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_maps)

        //val intent = intent
        //val message = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE)
        //val textView = findViewById(R.id.text_message) as TextView
        //textView.setText(message);

        var button = findViewById<Button>(R.id.button_create)

        button.setOnClickListener() {
            finishReplay()
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
        //val reply = mReply.getText().toString()

        val replyIntent = Intent()
        //replyIntent.putExtra(EXTRA_REPLY, reply)
        setResult(Activity.RESULT_OK, replyIntent)
        finish()
    }
}