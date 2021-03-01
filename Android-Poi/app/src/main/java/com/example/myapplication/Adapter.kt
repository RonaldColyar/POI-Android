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
