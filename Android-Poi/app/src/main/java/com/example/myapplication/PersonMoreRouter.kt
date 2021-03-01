package com.example.myapplication

import android.app.Dialog
import android.view.MenuItem
import layout.MainAdapter

class PersonMoreRouter (val adapter: MainAdapter){

    open fun edit_attribute_routing(it: MenuItem, position: Int){
        if(it.itemId == R.id.EditFirstOfPerson){
            adapter.person_handler.display_edit_person_activty(position,"first")
        }
        else if (it.itemId == R.id.EditLastOfPerson){
            adapter.person_handler.display_edit_person_activty(position,"last")
        }
        else if (it.itemId == R.id.EditHeightOfPerson ){
            adapter.person_handler.display_edit_person_activty(position,"height")
        }
        else if (it.itemId == R.id.EditLocationOfPerson){
            adapter.person_handler.display_edit_person_activty(position,"location")
        }
        else{
            adapter.person_handler.display_edit_person_activty(position,"race")
        }
    }
    open  fun item_click_routing(it: MenuItem, position: Int, parent: Dialog){
        if (it.itemId == R.id.AddEntryOfPerson){
            adapter.entry_handler.show_entry_creation_view(position,parent)
        }
        else if (it.itemId == R.id.ViewDetailsOfPerson){
            adapter.person_handler.show_detailed_person_view(position)
        }
        else {
            edit_attribute_routing(it,position)
        }
    }
}