package com.example.myapplication

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import layout.Entry
import layout.MainAdapter


/*

1.  PersonHandler displays layouts/Activities relating to a Person
2.  configures the layout to match the data of the Persons
3.  Sets event listeners for the Buttons and ImageViews of each child dialog*/
class PersonHandler(val context: Context, val adapter: MainAdapter){
    open fun display_edit_person_activty(position: Int,field:String){
        val first :String? = adapter.persons[position].first
        val last :String? =adapter. persons[position].last
        val id :String? = adapter.persons[position].id
        val action = Intent(context , editPersonAttribute::class.java)
        //adding data for label modification
        action.putExtra("first" , first)
        action.putExtra("last" , last)
        action.putExtra("field" , field )
        action.putExtra("id" ,id )
        val tempActivity = context as Activity
        tempActivity.startActivityForResult(action,4039)

    }

    open fun show_detailed_person_view (position: Int ){
        val dialog = adapter.generic_functionality.configured_dialog(R.layout.person_detail_view)
        val location = dialog.findViewById(R.id.personDetailsLocation) as TextView
        val race = dialog.findViewById(R.id.personDetailsRace) as TextView
        val height = dialog.findViewById(R.id.personDetailsHeight) as TextView
        val image = dialog.findViewById(R.id.detailedPersonImage) as ImageView
        adapter.generic_functionality.update_photo(image, Uri.parse(adapter.persons[position].image_path))
        location.text = adapter.persons[position].location
        race.text = adapter.persons[position].race
        height.text = adapter.persons[position].height
        dialog.show()
    }

    private fun configure_entry_divider(viewer: RecyclerView){
        var decorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.bluediv)!!)
        viewer.addItemDecoration(decorator)
    }
    private fun configure_layout_and_show(
        first: TextView, last: TextView, viewer: RecyclerView,
        position: Int, dialog: Dialog
    ){
        first.text = adapter.persons[position].first
        last.text = adapter.persons[position].last
        //gather person(s) entry data and pass to list(viewer)
        val cursor : Cursor = adapter.databaseFunctionality.gather_person_entries(adapter.persons[position].id!!)
        val entries : MutableList<Entry> = adapter.databaseFunctionality.formatt_entries(cursor)
        viewer.layoutManager = LinearLayoutManager(context)
        viewer.adapter = EntryAdapter(entries,context)
        configure_entry_divider(viewer)
        dialog.show()
    }

    open fun show_person_view(position: Int){
        val dialog = adapter.generic_functionality.configured_dialog(R.layout.personentryview)
        val first  = dialog.findViewById(R.id.EntryViewFirst) as TextView
        val last = dialog.findViewById(R.id.EntryViewLast) as TextView
        val list = dialog.findViewById(R.id.EntryViewList) as RecyclerView
        val image = dialog.findViewById(R.id.personEntryViewImage) as ImageView
        val more_actions = dialog.findViewById(R.id.morepersonactions) as ImageView
        //update labels/images to match the person data
        set_person_view_click_events(more_actions,position,dialog)
        adapter.generic_functionality.update_photo(image , Uri.parse(adapter.persons[position].image_path))
        configure_layout_and_show(first,last,list,position,dialog)
    }
    private fun set_person_view_click_events(more_actions: ImageView, position: Int, parent: Dialog){
        more_actions.setOnClickListener {
            show_actions_popup(more_actions,parent,position)
        }

    }
    private  fun show_actions_popup(view: View, parent: Dialog, position: Int){
        val popup = PopupMenu(context , view)
        popup.inflate(R.menu.more_person_options)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            adapter.person_entry_view_more_router.item_click_routing(it,position,parent)
            true
        })
        popup.show()
    }

}
