package layout

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.personsview.view.*
import com.example.myapplication.EntryAdapter

class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
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

    fun configure_layout(
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

    fun show_person_view(position: Int){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.personentryview)
        val first = dialog.findViewById(R.id.EntryViewFirst) as TextView
        val last  = dialog.findViewById(R.id.EntryViewLast) as TextView
        val list = dialog.findViewById(R.id.EntryViewList) as RecyclerView
        configure_layout(first,last,list,position,dialog)


    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //update_photo(holder.itemView.PersonImage , Uri.parse(persons[position].image_path))
        holder.itemView.firstLabel.text = persons[position].first
        holder.itemView.lastLabel.text = persons[position].last
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
