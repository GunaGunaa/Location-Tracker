package com.example.locationtracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationtracker.model.UserRealmModel
import com.example.locationtracker.repository.LTLocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LTUsersViewModel : ViewModel() {
    val usersList: MutableLiveData<ArrayList<UserRealmModel>> = MutableLiveData()
    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val allUsers = LTLocationRepository.getAllUsers()
            usersList.postValue(allUsers)
        }
    }
}