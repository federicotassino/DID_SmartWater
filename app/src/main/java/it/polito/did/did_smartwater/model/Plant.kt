package it.polito.did.did_smartwater.model

data class Plant(
    val stringResourceId: Int,
    val name: String,
    val irrigationMode: Int,
    val startDate: String,
    val irrigationDays: Int,
    val note: String
    ) {
}