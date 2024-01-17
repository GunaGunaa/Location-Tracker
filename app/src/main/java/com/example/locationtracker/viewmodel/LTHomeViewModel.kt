package com.example.locationtracker.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationtracker.model.LocationData
import com.example.locationtracker.repository.LTLocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LTHomeViewModel : ViewModel() {
    var locationList: MutableLiveData<ArrayList<LocationData>> = MutableLiveData()
    @SuppressLint("SuspiciousIndentation")
    fun getAllLocationList(mail:String) {
        viewModelScope.launch(Dispatchers.IO) {
        val allLocationData = LTLocationRepository.getAllLocationData(mail)
            locationList.postValue(allLocationData)
        }
    }
}