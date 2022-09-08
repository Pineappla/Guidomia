package com.example.guidomia.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.guidomia.CarDTO
import com.example.guidomia.repository.GuidomiaRepository
import kotlinx.coroutines.launch

class CarViewModel(application: Application): AndroidViewModel(application) {

    companion object{
        const val TAG = "CarViewModel"
    }

    private val repo = GuidomiaRepository.getInstance(application) //Used to perform data operations
    val carsLiveData: MutableLiveData<MutableList<CarDTO>> = MutableLiveData() //Used to update the UI
    val modelLiveData: MutableLiveData<MutableList<String>> = MutableLiveData()
    private val carsList: MutableList<CarDTO> = mutableListOf()

    /** 9/6/2022
    * This method is called by the view to load the data.
    *
    **/
    fun loadData(){
        //Launch a coroutine in the scope for database operations.
        viewModelScope.launch {
            repo.loadData{
                carsList.addAll(it)
                //Post the value so that view can update.
                carsLiveData.postValue(it)
            }
        }
    }

    fun getCarMakeList(): MutableList<String>{
        Log.e(TAG, "getCarMakeList: ${carsList.map { it.make }.toMutableList()}")
        return carsList.map { it.make }.toMutableList()
    }

    fun getCarModelList(): MutableList<String>{
        Log.e(TAG, "getCarMakeList: ${carsList.map { it.model }.toMutableList()}")
        return carsList.map { it.model }.toMutableList()
    }

}