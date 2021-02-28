package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_person_attribute.*

class editPersonAttribute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_person_attribute)
        val first  = intent.getStringExtra("first")
        val last =  intent.getStringExtra("last")
        val field = intent.getStringExtra("")
        UpdateAttributeLabel.text = "${first} ${last}'s  New " + field+":"
    }
}