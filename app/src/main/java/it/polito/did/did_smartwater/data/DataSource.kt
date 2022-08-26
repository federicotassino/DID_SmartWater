package it.polito.did.did_smartwater.data

import it.polito.did.did_smartwater.model.Plant
import it.polito.did.did_smartwater.R

class DataSource {
    fun loadPlants(): List<Plant>{
        return listOf<Plant>(
            Plant(R.string.plant1, "Basilico", 0, "23-08-22", 2, 0.5f, "note"),
            Plant(R.string.plant2, "Basilico", 0, "23-08-22", 2, 0.5f,"note"),
            Plant(R.string.plant3, "Basilico", 0, "23-08-22", 2, 0.5f,"note")
        )
    }
}