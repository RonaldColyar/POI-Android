package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_person_form.*
import layout.DatabaseFunctionality
import layout.Person
//start for result to gather person object for main activity
class new_person_form : AppCompatActivity() {
    lateinit var photo_data :Uri
    lateinit var database_manager :DatabaseFunctionality

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_person_form)

         database_manager = DatabaseFunctionality(this)
        SelectImageButton.setOnClickListener {
            select_photo()
        }
        create_person_button.setOnClickListener {
            if (data_is_present()){
                pass_and_close(
                    firstEntry.text.toString(),
                    lastEntry.text.toString(),
                    locationEntry.text.toString(),
                    raceEntry.text.toString(),
                    heightEntry.text.toString(),
                    photo_data.toString()
                    )

            }
            else{
                Toast.makeText(this, "Please Make sure all data is inserted!", Toast.LENGTH_LONG)
            }

        }
    }
    private fun pass_and_close(first:String,last:String,location:String,
                               race:String,height:String,image_path:String){
        var intent_data = Intent()
        intent_data.putExtra("first" , first)
        intent_data.putExtra("last",last)
        intent_data.putExtra("location",location)
        intent_data.putExtra("race", race)
        intent_data.putExtra("height", height)
        intent_data.putExtra("image_path",image_path)
        setResult(5949,intent_data)
        Toast.makeText(this, "User Created!!", Toast.LENGTH_LONG)
        this.finish() // close activity




    }
    private  fun data_is_present():Boolean{
        val entries = arrayOf(firstEntry,lastEntry,locationEntry,raceEntry,heightEntry) // widgets
        //check every user data input
        for ( editText in entries){
            if (editText.text.isNullOrEmpty() ){
                return false
            }

        }
        return true
    }

    private fun select_photo () {
        val photo_selection_intent = Intent(Intent.ACTION_PICK)
        photo_selection_intent.type ="image/*"
        startActivityForResult( photo_selection_intent , 33)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 33 && resultCode== Activity.RESULT_OK && data != null) {
             photo_data = data.data!!
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
