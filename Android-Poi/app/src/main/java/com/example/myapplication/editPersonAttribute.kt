package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_person_attribute.*

class editPersonAttribute : AppCompatActivity() {

    private fun close_and_pass_data(){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_person_attribute)
        val first  = intent.getStringExtra("first")
        val last =  intent.getStringExtra("last")
        val field = intent.getStringExtra("field")
        UpdateAttributeLabel.text = "${first} ${last}'s  New " + field+ ":"
        SubmitUpdate.setOnClickListener {

        }
    }
}