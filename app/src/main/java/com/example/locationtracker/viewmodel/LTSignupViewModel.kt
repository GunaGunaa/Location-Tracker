package com.example.locationtracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationtracker.model.UserRealmModel
import com.example.locationtracker.repository.LTLocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LTSignupViewModel : ViewModel() {
    var signupStatus: MutableLiveData<String> = MutableLiveData()


    fun saveUserDetails(userRealmModel: UserRealmModel) {
        viewModelScope.launch(Dispatchers.IO) {
           val userStatus = LTLocationRepository.insertUserData(userRealmModel)
            signupStatus.postValue(userStatus)
        }
    }
}