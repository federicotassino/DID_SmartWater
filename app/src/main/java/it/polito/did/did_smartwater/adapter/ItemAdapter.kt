package it.polito.did.did_smartwater.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation.findNavController
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
        val plant_item_name: TextView = view.findViewById(R.id.plant_item_name)
        val plant_item_irrigation_mode: ImageView =view.findViewById(R.id.plant_item_irrigation_mode)
        val plant_item_humidity: TextView = view.findViewById(R.id.plant_item_humidity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.plant_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // caricamento dati nel viewholder
        val item = dataset[position]
        holder.plant_item_name.text =  dataset[position].name
        holder.plant_item_humidity.text =  "Umidità: " + dataset[position].humidityLevel.toString()
        if (item.irrigationMode == 0){
            holder.plant_item_irrigation_mode.setImageResource(R.drawable.manual_mode_acceso)
        }
        if (item.irrigationMode == 1){
            holder.plant_item_irrigation_mode.setImageResource(R.drawable.timer_mode_acceso)
        }
        if (item.irrigationMode == 2){
            holder.plant_item_irrigation_mode.setImageResource(R.drawable.auto_mode_accesa)
        }


        //on click listener
        holder.plant_item_name.setOnClickListener(){
            //Plants.onItemClickedCompanion()

        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}