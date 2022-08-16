package it.polito.did.did_smartwater.data

import it.polito.did.did_smartwater.model.Plant
import it.polito.did.did_smartwater.R

class DataSource {
    fun loadPlants(): List<Plant>{
        return listOf<Plant>(
            Plant(R.string.plant1, "Basilico", ""),
            Plant(R.string.plant2, "Basilico", ""),
            Plant(R.string.plant3,"Basilico", ""),
            Plant(R.string.plant4,"Basilico", ""),
            Plant(R.string.plant5,"Basilico", ""),
            Plant(R.string.plant6,"Basilico", ""),
            Plant(R.string.plant7,"Basilico", ""),
            Plant(R.string.plant8,"Basilico", ""),
            Plant(R.string.plant9,"Basilico", ""),
            Plant(R.string.plant10,"Basilico", ""),
        )
    }
}