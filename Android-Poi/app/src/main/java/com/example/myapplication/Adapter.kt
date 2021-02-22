package layout

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.personsview.view.*
import com.example.myapplication.EntryAdapter
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
class MainAdapter(
                    var persons : MutableList<Person>,
                    val context:Context,
                    val databaseFunctionality: DatabaseFunctionality,var count :Int) : RecyclerView.Adapter<ViewHolder>() {

    private fun update_photo(Profileimage:ImageView , data:Uri){
        //gather file resource data
        val bitmapforphoto = MediaStore.Images.Media.getBitmap(context.contentResolver ,data)
        val bitmapdraw = BitmapDrawable(bitmapforphoto)
        //update U.I with image
        Profileimage.setImageResource(0)
        Profileimage.setBackgroundDrawable(bitmapdraw)
    }
    //date funct


    private fun configure_layout_and_show(
                first:TextView,
                last:TextView,
                viewer:RecyclerView,
                position: Int,
                dialog: Dialog){
        first.text = persons[position].first
        last.text = persons[position].last
         //gather entry data and pass to list(viewer)
        val cursor = databaseFunctionality.gather_person_entries(persons[position].id!!.toInt())
        val entries = databaseFunctionality.formatt_entries(cursor)
        viewer.layoutManager = LinearLayoutManager(context)
        viewer.adapter = EntryAdapter(entries,context)
        dialog.show()
    }

    private fun entry_creation_check(label:EditText, level:EditText,
                                     description:EditText, position: Int){

        if (label.text.isNullOrEmpty() || level.text.isNullOrEmpty() || description.text.isNullOrEmpty()){
            Toast.makeText(context, "Make Sure All Fields Are Populated!!" , Toast.LENGTH_LONG).show()
        }
        else{
            val values = ContentValues().apply{
                this.put("id" ,persons[position].id)
                this.put("label" , label.text.toString())
                this.put("data" , description.text.toString())
                this.put("level" , level.text.toString())
                this.put("date_string",DateManager().current_date())
            }
            databaseFunctionality.save_new_data(values,"entries2")
        }
    }

    private fun show_entry_creation_view(position: Int){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.activity_new_entry_form)
        val create_button = dialog.findViewById(R.id.CreateEntryButton) as Button
        create_button.setOnClickListener {
            //gather user input elements to store data
            val label = dialog.findViewById(R.id.EntryLabelEdit) as EditText
            val level  = dialog.findViewById(R.id.ThreatLevelEdit) as EditText
            val description = dialog.findViewById(R.id.DescriptionEdit) as EditText
            entry_creation_check(label,level,description,position)//check and store data
        }
        dialog.show()
    }

    private fun show_person_view(position: Int){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.personentryview)
        //gather elements for configuration(changing U.I)
        val first = dialog.findViewById(R.id.EntryViewFirst) as TextView
        val last  = dialog.findViewById(R.id.EntryViewLast) as TextView
        val list = dialog.findViewById(R.id.EntryViewList) as RecyclerView
        val add_entry = dialog.findViewById(R.id.addentry) as ImageView
        add_entry.setOnClickListener {
            show_entry_creation_view(position)
        }
        configure_layout_and_show(first,last,list,position,dialog)




    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //update_photo(holder.itemView.PersonImage , Uri.parse(persons[position].image_path))
        holder.itemView.firstLabel.text = persons[position].first
        holder.itemView.lastLabel.text = persons[position].last
        persons[position].image_path
        if (persons[position].image_path != "null"){
            update_photo(holder.itemView.PersonsImage, Uri.parse(persons[position].image_path))
        }
        holder.itemView.PersonsImage.setOnClickListener {
            //val first:String?,val last:String? , val location:String?,val race:String?,val height:String?,val image_path :String?
            show_person_view(position)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutinflation = LayoutInflater.from(parent?.context)
        val cellforrow = layoutinflation.inflate(R.layout.personsview,parent, false)
        return ViewHolder(cellforrow)    }

    override fun getItemCount(): Int {
        return count
    }
}
