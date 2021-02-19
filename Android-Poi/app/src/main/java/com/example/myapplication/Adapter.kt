package layout

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.personsview.view.*

class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
class MainAdapter(var persons : MutableList<Person> , val context:Context) : RecyclerView.Adapter<ViewHolder>() {

    private fun update_photo(Profileimage:ImageView , data:Uri){
        //gather file resource data
        val bitmapforphoto = MediaStore.Images.Media.getBitmap(context.contentResolver ,data)
        val bitmapdraw = BitmapDrawable(bitmapforphoto)
        //update U.I with image
        Profileimage.setImageResource(0)
        Profileimage.setBackgroundDrawable(bitmapdraw)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //update_photo(holder.itemView.PersonImage , Uri.parse(persons[position].image_path))
        holder.itemView.firstLabel.text = persons[position].first
        holder.itemView.lastLabel.text = persons[position].last
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutinflation = LayoutInflater.from(parent?.context)
        val cellforrow = layoutinflation.inflate(R.layout.personsview,parent, false)
        return ViewHolder(cellforrow)    }

    override fun getItemCount(): Int {
        return persons.size
    }
}
