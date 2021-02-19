package layout

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.entryviewchild.view.*

class EntryAdapter(var entries :MutableList<Entry> , val context:Context) : RecyclerView.Adapter<ViewHolder>() {

    private fun show_details(data:Entry) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.detailedentryview)
        var datelabel = dialog.findViewById(R.id.detailedDate) as TextView
        var entrylabel = dialog.findViewById(R.id.detailedLabel) as TextView
        var levellabel = dialog.findViewById(R.id.detailedLabel) as TextView
        //modify the values to fit entry data before showing
        datelabel.text = data.date_string
        entrylabel.text = data.label
        levellabel.text = data.level
        dialog.show()

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.EntryChildLabel.text = entries[position].label
        holder.itemView.EntryChildDate.text = entries[position].date_string
        holder.itemView.EntryChildLevel.text = entries[position].level
        holder.itemView.EntryChildLabel.setOnClickListener {
            show_details(entries[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutinflation = LayoutInflater.from(parent?.context)
        val cellforrow = layoutinflation.inflate(R.layout.entryviewchild,parent, false)
        return ViewHolder(cellforrow)    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}