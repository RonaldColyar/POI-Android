package layout

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.EntryAdapter
import kotlinx.android.synthetic.main.personsview.view.*
import java.text.SimpleDateFormat
import java.util.*

class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)


class DateManager{
    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    open fun current_date():String{
        val date = DateManager().getCurrentDateTime()
        return date.toString("yyyy/MM/dd")
    }

}


class GenericFunctionality(val context: Context){
    open fun configured_dialog(id:Int):Dialog{
        val dialog  = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(id)
        return dialog
    }
    open fun update_photo(Profileimage:ImageView , data:Uri){
        try {
            //gather file resource data
            val bitmapforphoto = MediaStore.Images.Media.getBitmap(context.contentResolver, data)
            val bitmapdraw = BitmapDrawable(bitmapforphoto)
            //update U.I with image
            Profileimage.setImageResource(0)
            Profileimage.setBackgroundDrawable(bitmapdraw)
        }
        catch (e:Throwable){

        }
    }
}
class PersonHandler(val context: Context , val adapter: MainAdapter){

    private fun configure_entry_divider(viewer: RecyclerView){
        var decorator = DividerItemDecoration(context,LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.bluediv)!!)
        viewer.addItemDecoration(decorator)
    }
    private fun configure_layout_and_show(
        first:TextView, last:TextView, viewer:RecyclerView,
        position: Int, dialog: Dialog ){
        first.text = adapter.persons[position].first
        last.text = adapter.persons[position].last
        //gather person(s) entry data and pass to list(viewer)
        val cursor :Cursor = adapter.databaseFunctionality.gather_person_entries(adapter.persons[position].id!!)
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
    private fun set_person_view_click_events( more_actions:ImageView, position: Int,parent:Dialog){
        more_actions.setOnClickListener {
            show_actions_popup(more_actions,parent,position)
        }

    }
    private  fun show_actions_popup(view:View , parent: Dialog,position: Int){
        val popup = PopupMenu(context , view)
        popup.inflate(R.menu.more_person_options)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            adapter.person_entry_view_more_router.item_click_routing(it,position,parent)
            true
        })
        popup.show()
    }

}
class EntryHandler(val context: Context , val adapter: MainAdapter){
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
    private fun configured_values(label:EditText, level:EditText,
                                  description:EditText, position: Int):ContentValues{
        val values :ContentValues = ContentValues().apply{
            this.put("id" ,adapter.persons[position].id)
            this.put("label" , label.text.toString())
            this.put("data" , description.text.toString())
            this.put("level" , level.text.toString())
            this.put("date_string",DateManager().current_date())
        }
        return values
    }
    private fun entry_creation_check(label:EditText, level:EditText,
                                     description:EditText, position: Int,
                                     parent: Dialog,child:Dialog){
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
        val dialog :Dialog = adapter.generic_functionality.configured_dialog(R.layout.activity_new_entry_form)
        val create_button : Button = dialog.findViewById(R.id.CreateEntryButton) as Button
        create_button.setOnClickListener {
            //gather user input elements to store data
            val label :EditText = dialog.findViewById(R.id.EntryLabelEdit) as EditText
            val level  :EditText = dialog.findViewById(R.id.ThreatLevelEdit) as EditText
            val description :EditText = dialog.findViewById(R.id.DescriptionEdit) as EditText
            entry_creation_check(label,level,description,position,parent,dialog)//check and store data
        }
        dialog.show()
    }

}

class PersonMoreRouter (val adapter:MainAdapter){

    open fun edit_attribute_routing(it:MenuItem , position: Int){
        if(it.itemId == R.id.EditFirstOfPerson){
            adapter.display_edit_person_activty(position,"first")
        }
        else if (it.itemId == R.id.EditLastOfPerson){
            adapter.display_edit_person_activty(position,"last")
        }
        else if (it.itemId == R.id.EditHeightOfPerson ){
            adapter.display_edit_person_activty(position,"height")
        }
        else if (it.itemId == R.id.EditLocationOfPerson){
            adapter.display_edit_person_activty(position,"location")
        }
        else{
            adapter.display_edit_person_activty(position,"race")
        }
    }
    open  fun item_click_routing(it: MenuItem,position: Int,parent: Dialog){
        if (it.itemId == R.id.AddEntryOfPerson){
            adapter.entry_handler.show_entry_creation_view(position,parent)
        }
        else if (it.itemId == R.id.ViewDetailsOfPerson){
            adapter.show_detailed_person_view(position)
        }
        else {
            edit_attribute_routing(it,position)
        }
    }
}
class MainAdapter(
                    var persons : MutableList<Person>,
                    val context:Context,
                    val databaseFunctionality: DatabaseFunctionality,
                    var count :Int) : RecyclerView.Adapter<ViewHolder>() {
    val entry_handler = EntryHandler(context,this)
    val person_entry_view_more_router = PersonMoreRouter(this)
    val generic_functionality = GenericFunctionality(context)
    val person_handler = PersonHandler(context,this)



    open fun remove_person(id:String){
        //remove every person that matched the id
        for (person in persons){
            if (person.id == id ){
                persons.remove(person)
                count -=1
                return
            }
        }
    }





    open fun display_edit_person_activty(position: Int,field:String){
        val first :String? = persons[position].first
        val last :String? = persons[position].last
        val id :String? = persons[position].id
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
        val dialog = generic_functionality.configured_dialog(R.layout.person_detail_view)
        val location = dialog.findViewById(R.id.personDetailsLocation) as TextView
        val race = dialog.findViewById(R.id.personDetailsRace) as TextView
        val height = dialog.findViewById(R.id.personDetailsHeight) as TextView
        val image = dialog.findViewById(R.id.detailedPersonImage) as ImageView
        generic_functionality.update_photo(image, Uri.parse(persons[position].image_path))
        location.text = persons[position].location
        race.text = persons[position].race
        height.text = persons[position].height
        dialog.show()
    }

    private fun set_click_events(position: Int, holder: ViewHolder){
        holder.itemView.deletePerson.setOnClickListener {
            val temp = context as Activity
            val action = Intent(context,SensitiveDataConfirm::class.java)
            action.putExtra("id" , persons[position].id)
            action.putExtra("type" , "person")
            temp.startActivityForResult(action,3008)
        }
        holder.itemView.PersonsImage.setOnClickListener {
            person_handler.show_person_view(position)

        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //update_photo(holder.itemView.PersonImage , Uri.parse(persons[position].image_path))
        holder.itemView.firstLabel.text = persons[position].first
        holder.itemView.lastLabel.text = persons[position].last
        if (persons[position].image_path != "null"){
            generic_functionality.update_photo(holder.itemView.PersonsImage, Uri.parse(persons[position].image_path))
        }
        set_click_events(position,holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutinflation = LayoutInflater.from(parent?.context)
        val cellforrow :View = layoutinflation.inflate(R.layout.personsview,parent, false)
        return ViewHolder(cellforrow)    }

    override fun getItemCount(): Int {
        return count
    }
}
