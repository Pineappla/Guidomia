package com.example.guidomia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.guidomia.CarDTO
import com.example.guidomia.entity.EntityCar

@Dao
interface CarDao {

    @Query("SELECT Customer_price AS customerPrice, Market_price AS marketPrice," +
            "Make AS make, Model AS model, pros_list AS prosList, cons_list AS consList, rating FROM EntityCar")
    suspend fun getCars(): MutableList<CarDTO>

    @Insert
    suspend fun insert(list: MutableList<EntityCar>): List<Long>

}