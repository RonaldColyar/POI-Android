package com.example.myapplication

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import layout.MainAdapter

/*EntryHandler manages the creation of entries of a person,
            and the entry creation logic/checks */
class EntryHandler(val context: Context, val adapter: MainAdapter){
    private fun level_input_check(level:String) :Boolean{
        var status :Boolean = false
        if (level == "1"){
            status = true
        }
        else if (level == "2"){
            status = true
        }
        else if (level == "3"){
            status = true
        }
        return status
    }
    private fun configured_values(label: EditText, level: EditText,
                                  description: EditText, position: Int): ContentValues {
        val values : ContentValues = ContentValues().apply{
            this.put("id" ,adapter.persons[position].id)
            this.put("label" , label.text.toString())
            this.put("data" , description.text.toString())
            this.put("level" , level.text.toString())
            this.put("date_string",DateManager().current_date())
        }
        return values
    }
    private fun entry_creation_check(label: EditText, level: EditText,
                                     description: EditText, position: Int,
                                     parent: Dialog, child: Dialog
    ){
        if (label.text.isNullOrEmpty() || level.text.isNullOrEmpty() || description.text.isNullOrEmpty()){
            Toast.makeText(context, "Make Sure All Fields Are Populated!!" , Toast.LENGTH_LONG).show()
        }
        else if (level_input_check(level.text.toString()) == false){
            Toast.makeText(context , "Make Sure the threat level is numbers 1-3" , Toast.LENGTH_LONG).show()
        }
        else{
            //save data and close layout
            adapter.databaseFunctionality.save_new_data( configured_values(label,level,description,position),"entries2")
            Toast.makeText(context, "Success!" , Toast.LENGTH_LONG).show()
            parent.hide()
            child.hide()
        }
    }

    open fun show_entry_creation_view(position: Int,parent: Dialog){
        val dialog : Dialog = adapter.generic_functionality.configured_dialog(R.layout.activity_new_entry_form)
        val create_button : Button = dialog.findViewById(R.id.CreateEntryButton) as Button
        create_button.setOnClickListener {
            //gather user input elements to store data
            val label : EditText = dialog.findViewById(R.id.EntryLabelEdit) as EditText
            val level  : EditText = dialog.findViewById(R.id.ThreatLevelEdit) as EditText
            val description : EditText = dialog.findViewById(R.id.DescriptionEdit) as EditText
            entry_creation_check(label,level,description,position,parent,dialog)//check and store data
        }
        dialog.show()
    }

}