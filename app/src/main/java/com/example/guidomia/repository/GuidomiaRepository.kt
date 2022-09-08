package com.example.guidomia.repository

import android.app.Application
import android.content.res.AssetManager
import com.example.guidomia.CarDTO
import com.example.guidomia.GuidomiaDatabase
import com.example.guidomia.entity.EntityCar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream

class GuidomiaRepository(
    database: GuidomiaDatabase,
    private val assetManager: AssetManager,
) {

    companion object {
        const val TAG = "GuidomiaRepo"

        /** 9/6/2022
        * This method will return the instance of the repository
        * @param application -> Used to initialize the database and assetManager.
        **/
        fun getInstance(application: Application): GuidomiaRepository {
            return GuidomiaRepository(GuidomiaDatabase.getDatabase(application), application.assets)
        }
    }

    private val carDao = database.carDao() //Used to access Car table

    /** 9/6/2022
     * This method will load the data and return the list of cars via the callback
     * @param callback -> Used to return the list of cars to the caller
     **/
    suspend fun loadData(callback: (MutableList<CarDTO>) -> Unit) = withContext(Dispatchers.IO) {

        //First, try to get the data from the database
        carDao.getCars().let {
            //If the list is empty, then load the asset file.
            if (it.isEmpty()) {
                loadAssetFile(callback)
            } else {
                //proceed with the data
                callback.invoke(it)
            }
        }

    }

    /** 9/6/2022
     * This method will load the asset file and insert the values in the database.
     * @param callback -> Used to return the list of cars to the caller
     **/
    private suspend fun loadAssetFile(callback: (MutableList<CarDTO>) -> Unit) =
        withContext(Dispatchers.IO) {

            //Open the asset file input stream.
            assetManager.open("car_list.json").let { stream ->

                //Create a buffered stream and byte array to hold the data
                val bis = BufferedInputStream(stream)
                val byteArray = ByteArray(stream.available())
                val stringBuilder = StringBuilder()

                //Read the data in stream and append them in string builder.
                while (bis.read(byteArray) != -1) {
                    stringBuilder.append(String(byteArray))
                }

                //Close the stream
                bis.close()

                //Create the list of entity models from json model to be inserted into the database.
                val carsList = Gson().fromJson<MutableList<EntityCar>>(
                    stringBuilder.toString(),
                    object : TypeToken<MutableList<EntityCar>>() {}.type
                )

                //Store the array of rows inserted to check the progress of the operation
                val result = carDao.insert(carsList)

                if (result.isNotEmpty()) {
                    //Also, create the array of DTO to be returned to the caller.
                    val carDtos = mutableListOf<CarDTO>()
                    carsList.forEach {
                        carDtos.add(
                            CarDTO(
                                it.customerPrice,
                                it.marketPrice,
                                it.make,
                                it.model,
                                it.prosList,
                                it.consList,
                                it.rating
                            )
                        )
                    }
                    //Invoke the callback to return the result to the caller.
                    callback.invoke(carDtos)
                }
            }
        }
}