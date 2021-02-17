package layout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class EntryAdapter(var entries :MutableList<Entry>) : RecyclerView.Adapter<ViewHolder>() {
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutinflation = LayoutInflater.from(parent?.context)
        val cellforrow = layoutinflation.inflate(R.layout.entryviewchild,parent, false)
        return ViewHolder(cellforrow)    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}