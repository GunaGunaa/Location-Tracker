package com.example.locationtracker.repository

import android.util.Log
import android.widget.Toast
import com.example.locationtracker.LTApplicationClass
import com.example.locationtracker.model.UserRealmModel
import io.realm.Realm
import io.realm.kotlin.where

object LTLocationRepository {

    fun insertUserData(userRealmModel: UserRealmModel): String {
         val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val existingUser = realm.where(UserRealmModel::class.java)
            .equalTo("mail", userRealmModel.mail)
            .findFirst()
        return if (existingUser == null) {
            realm.copyToRealm(userRealmModel)
            realm.commitTransaction()
            realm.close()
            "Success"
        } else {
            realm.close()
            "Fail"
        }
    }

    fun checkLogin(mail: String, password: String): Boolean {
      val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val isLoginUser = realm.where(UserRealmModel::class.java)
            .equalTo("mail", mail)
            .equalTo("password", password)
            .findFirst()
        realm.close()
       return isLoginUser!=null
    }
}