package it.polito.did.did_smartwater.data

import it.polito.did.did_smartwater.model.Plant
import it.polito.did.did_smartwater.R

class DataSource {
    fun loadPlants(): List<Plant>{
        return listOf<Plant>(
            Plant(R.string.plant1),
            Plant(R.string.plant2),
            Plant(R.string.plant3),
            Plant(R.string.plant4),
            Plant(R.string.plant5),
            Plant(R.string.plant6),
            Plant(R.string.plant7),
            Plant(R.string.plant8),
            Plant(R.string.plant9),
            Plant(R.string.plant10),
        )
    }
}