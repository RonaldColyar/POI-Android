package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_mainhub.*
import layout.DatabaseFunctionality
import layout.MainAdapter
import layout.Person

class mainhub : AppCompatActivity() {
    lateinit var adapter : MainAdapter
    lateinit var databaseFunctionality: DatabaseFunctionality
    private fun show_popup(view: View){
        val popup = PopupMenu(this , view)
        popup.inflate(R.menu.dropdown)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {

            if (it.itemId == R.id.addPerson)
            {
              val action =  Intent(this, new_person_form::class.java)
              startActivityForResult(action,200)
            }
            else if (it.itemId == R.id.breached){
               val action  = Intent(this,SensitiveDataConfirm::class.java)
               action.putExtra("type" , "BREACHED")
               action.putExtra("code", intent.getStringExtra("code"))
               startActivityForResult(action,3009)
            }
            true
        })
        popup.show()
    }
    private fun check_rows_affected(rows:Int , id:String?){
        if (rows <1){
            Toast.makeText(this,"Issue Deleting Person!" , Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this,"Successfully Removed Person", Toast.LENGTH_LONG).show()
            //update persons list held by adapter(in memory)
            adapter.remove_person(id!!)
            modify_default_label_if_needed(adapter.count,"removal")
            //re-bind the recyclerview so the person is no longer in list(visually)
            ProfileView.adapter = adapter
        }
    }

    private fun check_person_deletion_code(data: Intent?) {
        if (data!!.getStringExtra("code") == intent.getStringExtra("code")) {
            val rows_affected =
                databaseFunctionality.db.delete(
                    "persons2",
                    "id=?",
                    arrayOf(data.getStringExtra("id"))
                )
            check_rows_affected(rows_affected , data.getStringExtra("id"))
        } else {
            //closing application for security
            this.finish()
        }
    }

    private fun check_breached_deletion_code(data: Intent?){
        if (data!!.getStringExtra("code") == intent.getStringExtra("code")) {
            databaseFunctionality.delete_everything("persons2")
            databaseFunctionality.delete_everything("entries2")
            //update persons list held by adapter(in memory)
            adapter.persons = mutableListOf<Person>()
            adapter.count = 0
            modify_default_label_if_needed(adapter.count,"removal")
            //re-bind the recyclerview so the persons are no longer in list(visually)
            ProfileView.adapter = adapter
        }
        else{
            //closing application for security
            this.finish()
        }
    }

    private fun check_for_deletions(requestCode: Int , data: Intent?){
        if(requestCode == 3008 && data != null){//deleting a person
                check_person_deletion_code(data)
              }
         else if (requestCode == 3009 && data != null){//deleting everything(breach protocol)
            check_breached_deletion_code(data)
        }
    }

    private  fun check_update_results(code:Int){
        if (code == 0){
            Toast.makeText(this, "Successfully updated!!", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Issue Updating!" , Toast.LENGTH_LONG).show()
        }
    }


    private  fun check_for_person_modification(requestCode: Int , data: Intent?){
        if (requestCode == 4039 && data != null){
            val values = ContentValues().apply {
                this.put(data.getStringExtra("key" ) , data.getStringExtra("value"))
                }
            val result = databaseFunctionality.db.update(
                "persons2" ,
                values,
                data.getStringExtra("whereClause") ,
                arrayOf(data.getStringExtra("wherearg")))
            check_update_results(result)
        }
    }
    private fun modify_default_label_if_needed(count:Int,type:String){
            if (count == 0 && type == "appending"){
                NullTextView.alpha = 0f
            }
            else if (count == 0 && type == "removal"){
                NullTextView.alpha = 1f
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode== 5949 && data != null){
            val person = Person(
                data.getStringExtra("first"), data.getStringExtra("last"),
                data.getStringExtra("location"), data.getStringExtra("race"),
                data.getStringExtra("height"), data.getStringExtra("image_path"),
                data.getStringExtra("id")
            )
            modify_default_label_if_needed(adapter.count,"appending")
            adapter.persons.add(person)
            adapter.count +=1
            //re-bind the recyclerview so the person is  in the list(visually)
            ProfileView.adapter = adapter
        }
        else{
            check_for_deletions(requestCode,data)
            check_for_person_modification(resultCode,data)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    fun setup_recyclerview(){
        var decorator = DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.dividerdraw)!!)
        ProfileView.layoutManager = LinearLayoutManager(this)
        ProfileView.adapter = adapter
        ProfileView.addItemDecoration(decorator)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainhub)
        optionsButton.setOnClickListener {
            show_popup(optionsButton)
        }
        databaseFunctionality = DatabaseFunctionality(this)
        val persons = databaseFunctionality.formatt_persons(databaseFunctionality.gather_persons())
        if (persons.isNotEmpty()){
            NullTextView.alpha = 0.0f
        }
        adapter = MainAdapter(persons , this,databaseFunctionality,persons.size)
        setup_recyclerview()


    }
}