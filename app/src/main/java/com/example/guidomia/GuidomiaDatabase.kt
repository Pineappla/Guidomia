package com.example.guidomia

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.guidomia.dao.CarDao
import com.example.guidomia.entity.EntityCar
import com.example.guidomia.entity.ListConverter

@Database(entities = [EntityCar::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class GuidomiaDatabase : RoomDatabase() {

    abstract fun carDao(): CarDao

    companion object{

        const val DB_NAME = "GuidomiaDB"

        @Volatile
        private var INSTANCE: GuidomiaDatabase? = null

        fun getDatabase(context: Context): GuidomiaDatabase{
            val temp = INSTANCE
            temp?.let {
                return it
            } ?: run {
                val database = Room.databaseBuilder(
                    context,
                    GuidomiaDatabase::class.java, DB_NAME
                )
                    .build()
                INSTANCE = database
                return database
            }
        }

    }
}