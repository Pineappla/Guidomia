package com.example.guidomia.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
class EntityCar: Serializable {

    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "Customer_price")
    @SerializedName("customerPrice")
    var customerPrice: Long = 0L

    @ColumnInfo(name = "Market_price")
    @SerializedName("marketPrice")
    var marketPrice: Long = 0L

    @ColumnInfo(name = "Make")
    @SerializedName("make")
    var make: String = ""

    @ColumnInfo(name = "Model")
    @SerializedName("model")
    var model: String = ""

    @ColumnInfo(name = "pros_list")
    @SerializedName("prosList")
    var prosList: MutableList<String> = mutableListOf()

    @ColumnInfo(name = "cons_list")
    @SerializedName("consList")
    var consList: MutableList<String> = mutableListOf()

    @ColumnInfo(name = "rating")
    @SerializedName("rating")
    var rating: Int = 0
}