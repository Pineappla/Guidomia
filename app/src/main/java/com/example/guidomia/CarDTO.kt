package com.example.guidomia

import androidx.room.Ignore

data class CarDTO(
    val customerPrice: Long, val marketPrice: Long, val make: String, val model: String,
    val prosList: MutableList<String>, val consList: MutableList<String>, val rating: Int
) {

    @Ignore
    var selected: Boolean = false

    fun getCarName(): String {
        return "${this.make} ${this.model}"
    }

    fun getPrice(): String {
        return "${this.marketPrice.div(1000)}k"
    }

}
