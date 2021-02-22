package com.example.myapplication


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import layout.DatabaseFunctionality
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    val CODE  = "0" //for local authentication
    var wrong_counter = 0
    private fun have_reading_permission() =
           ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    fun check_incorrect_attempts(){
        //remove all data for safety
        if (wrong_counter>10){
            val databaseFunctionality = DatabaseFunctionality(this)
            databaseFunctionality.delete_everything(this,"persons2")
            databaseFunctionality.delete_everything(this,"entries2")
        }
        else{
            wrong_counter+=1
        }
    }

    fun check_code_and_permission(code:String){
        if (code == CODE){
            if (have_reading_permission()){
                val action  = Intent(this , mainhub::class.java)
                startActivity(action)
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
            }
        }
        else{
            check_incorrect_attempts()
            Toast.makeText(this, "??/>?" , Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        submitButton.setOnClickListener {
            check_code_and_permission(code.text.toString())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED && requestCode == 0){
            val action  = Intent(this , mainhub::class.java)
            startActivity(action)
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}