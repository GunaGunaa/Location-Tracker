package com.example.locationtracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationtracker.repository.LTLocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LTLoginViewModel : ViewModel() {
    var loginStatus: MutableLiveData<Boolean> = MutableLiveData()
    fun checkLoginCredentials(mail:String,password:String){
        viewModelScope.launch(Dispatchers.IO) {
            val checkLogin = LTLocationRepository.checkLogin(mail, password)
            loginStatus.postValue(checkLogin)
        }
    }
}