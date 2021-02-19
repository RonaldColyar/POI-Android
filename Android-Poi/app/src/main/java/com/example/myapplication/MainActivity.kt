package com.example.myapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    var CODE  = "0"
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


}