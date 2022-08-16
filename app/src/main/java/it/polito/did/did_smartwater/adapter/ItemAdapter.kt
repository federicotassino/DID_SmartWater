package it.polito.did.did_smartwater.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import it.polito.did.did_smartwater.R
import it.polito.did.did_smartwater.model.Plant
import it.polito.did.did_smartwater.ui.main.Plants

class ItemAdapter(
    private val context: Context,
    private val dataset: List<Plant>,
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.plant_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.plant_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text =  dataset[position].name
        holder.textView.setOnClickListener(){
            //Plants.onItemClickedCompanion()
            holder.textView.text = "Cliccato"
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}