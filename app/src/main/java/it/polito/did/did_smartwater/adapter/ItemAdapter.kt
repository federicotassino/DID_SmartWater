package it.polito.did.did_smartwater.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import it.polito.did.did_smartwater.R
import it.polito.did.did_smartwater.model.Plant
import kotlinx.coroutines.tasks.await


class ItemAdapter(
    private val context: Context,
    private val dataset: List<Plant>,
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val plant_item_name: TextView = view.findViewById(R.id.plant_item_name)
        val plant_item_irrigation_mode: ImageView =view.findViewById(R.id.plant_item_irrigation_mode)
        val plant_item_humidity: TextView = view.findViewById(R.id.plant_item_humidity)
        val plant_item_profile_photo: ImageView = view.findViewById(R.id.imageViewProfilePhoto)
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
        holder.plant_item_humidity.text =  dataset[position].humidityLevel.toString()
        if (item.irrigationMode == 0){
            holder.plant_item_irrigation_mode.setImageResource(R.drawable.manual_mode_acceso)
        }
        if (item.irrigationMode == 1){
            holder.plant_item_irrigation_mode.setImageResource(R.drawable.timer_mode_acceso)
        }
        if (item.irrigationMode == 2){
            holder.plant_item_irrigation_mode.setImageResource(R.drawable.auto_mode_accesa)
        }

        /*val storage = FirebaseStorage.getInstance().getReference().child("foto")
        val photoBytes = storage.getBytes(10000).result
        val bmp = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)*/
        holder.plant_item_profile_photo.setImageBitmap(dataset[position].bmp)


        //on click listener
        holder.plant_item_name.setOnClickListener(){
            //Plants.onItemClickedCompanion()

        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}