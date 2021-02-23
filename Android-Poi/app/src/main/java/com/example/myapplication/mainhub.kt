package com.example.myapplication

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
    private fun display_layout(layout :Int ){
        val dialog_view = LayoutInflater.from(this).inflate(layout,null)
        val dialog_builder = AlertDialog.Builder(this)
        dialog_builder.setView(dialog_view)
        dialog_builder.show()
    }
    private fun show_popup(view: View){
        val popup = PopupMenu(this , view)
        popup.inflate(R.menu.dropdown)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            //event listeners for items
            if (it.itemId == R.id.addPerson)
            {

              val action =  Intent(this, new_person_form::class.java)
              startActivityForResult(action,200)
            }
            else if (it.itemId == R.id.breached){

            }

            true
        })
        popup.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode== 5949 && data != null){
            val person = Person(
                data.getStringExtra("first"),
                data.getStringExtra("last"),
                data.getStringExtra("location"),
                data.getStringExtra("race"),
                data.getStringExtra("height"),
                data.getStringExtra("image_path"),
                data.getStringExtra("id")

            )
            adapter.persons.add(person)
            adapter.count +=1
            ProfileView.adapter = adapter

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