package com.example.myapplication
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.entryviewchild.view.*
import layout.DatabaseFunctionality
import layout.Entry
import layout.ViewHolder

class EntryAdapter(var entries :MutableList<Entry>, val context:Context) : RecyclerView.Adapter<ViewHolder>() {

    private fun update_level_image(default_image: ImageView, level: String){
        Toast.makeText(context,level,Toast.LENGTH_LONG).show()
        if (level == "1") {
            default_image.setImageResource(R.drawable.level1)
        }
        else if (level == "2"){
            default_image.setImageResource(R.drawable.level2)
        }
        else{
            default_image.setImageResource(R.drawable.level3)
        }
    }

    private fun configure_layout(data: Entry,dialog: Dialog){
        val entrylabel = dialog.findViewById(R.id.detailedLabel) as TextView
        val datatext = dialog.findViewById(R.id.detailedData) as TextView
        val cancelbutton =dialog.findViewById(R.id.CloseDialogButton) as Button
        val deletebutton = dialog.findViewById(R.id.deleteEntryButton) as Button
        entrylabel.text = data.label
        datatext.text = data.data
        cancelbutton.setOnClickListener {
            dialog.hide()
        }
        deletebutton.setOnClickListener {
                DatabaseFunctionality(context).db.delete("entries2", "id =?  AND label = ?" , arrayOf(data.id,data.label))
                dialog.hide()
        }
    }
    private fun show_entry_details(data:Entry) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.detailedentryview)
        //update labels/images to match the data:Entry
        configure_layout(data,dialog)
        update_level_image(dialog.findViewById(R.id.detailedLevelPhoto), data.level)
        dialog.show()


    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.EntryChildLabel.text = entries[position].label
        holder.itemView.EntryChildDate.text = entries[position].date_string
        update_level_image(holder.itemView.EntryLevelPhoto,entries[position].level)
        holder.itemView.EntryChildLabel.setOnClickListener {
            show_entry_details(entries[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutinflation = LayoutInflater.from(parent?.context)
        val cellforrow = layoutinflation.inflate(R.layout.entryviewchild,parent, false)
        return ViewHolder(cellforrow)    }

    override fun getItemCount(): Int {
        return entries.size
    }
}