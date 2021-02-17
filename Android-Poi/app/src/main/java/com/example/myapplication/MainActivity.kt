package com.example.myapplication

import android.app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import layout.Person


class MainActivity : AppCompatActivity() {
    var CODE  = "USER_CONFIGURABLE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submitButton.setOnClickListener {
            if (code.text.toString() == CODE){
                val action  = Intent(this , mainhub::class.java)
                startActivity(action)
            }
            else{
                //confusion on U.I.
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode== 5949 && data != null){
            val person = Person(
                data.getStringExtra("first"),
                data.getStringExtra("last"),
                data.getStringExtra("location"),
                data.getStringExtra("race"),
                data.getStringExtra("height"),
                data.getStringExtra("image_path")

            )
        }


        super.onActivityResult(requestCode, resultCode, data)
    }
}