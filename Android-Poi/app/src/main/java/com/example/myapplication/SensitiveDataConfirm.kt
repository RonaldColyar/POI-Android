package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sensitive_data_confirm.*

class SensitiveDataConfirm : AppCompatActivity() {
    //pass data to result checker(located in mainhub activity)

    private fun check_type_and_pass(){
        val data = Intent()
        if (intent.getStringExtra("type") == "person") {
            data.putExtra("code", SensitiveCodeInput.text.toString())
            data.putExtra("id" , intent.getStringExtra("id"))
        }
        else{
            data.putExtra("code", SensitiveCodeInput.text.toString())
        }
        setResult(202,data)
        this.finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensitive_data_confirm)
        CloseSensitiveActivity.setOnClickListener {
            this.finish()
        }
        ConfirmSensitiveDeletion.setOnClickListener {
            check_type_and_pass()
        }
    }
}