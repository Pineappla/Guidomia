package com.example.guidomia.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {

    @TypeConverter
    fun fromProsList(value: String): MutableList<String>{
        return Gson().fromJson<MutableList<String>>(value,object: TypeToken<MutableList<String>>(){}.type )
    }

    @TypeConverter
    fun toProsList(list: MutableList<String>): String{
        return Gson().toJson(list)
    }

}