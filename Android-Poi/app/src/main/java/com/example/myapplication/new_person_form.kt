package com.example.myapplication

import android.app.Activity
import android.content.ContentValues
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
    var photo_data :Uri? = null
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
                Toast.makeText(this, "Please Make sure all data is inserted!", Toast.LENGTH_LONG).show()
            }

        }
    }

    //places data in database
    private fun store_person(first:String,last:String,location:String,
                             race:String,height:String,image_path:String){
            val values = ContentValues().apply {
                this.put("first" , first)
                this.put("last", last)
                this.put("location" , location)
                this.put("race", race)
                this.put("height", height)
                this.put("imagepath",image_path)
            }
            database_manager.save_new_data(values,"persons2")
    }
    //passes data to mainhub activity for list update
    private fun pass_and_close(first:String,last:String,location:String,
                               race:String,height:String,image_path:String){
        store_person(first,last,location,race,height,image_path)
        var intent_data = Intent()
        intent_data.putExtra("first" , first)
        intent_data.putExtra("last",last)
        intent_data.putExtra("location",location)
        intent_data.putExtra("race", race)
        intent_data.putExtra("height", height)
        intent_data.putExtra("image_path",image_path)
        Toast.makeText(this, "User Created!!", Toast.LENGTH_LONG)
        setResult(5949,intent_data)
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
        else{
            Toast.makeText(this ," Image Selection Canceled",Toast.LENGTH_LONG).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
