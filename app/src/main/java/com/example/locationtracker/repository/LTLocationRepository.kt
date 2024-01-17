package com.example.locationtracker.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.locationtracker.model.LocationData
import com.example.locationtracker.model.UserRealmModel
import com.example.locationtracker.model.UsersData
import io.realm.Realm
import io.realm.RealmResults

object LTLocationRepository {

    /**
     * to insert the user data
     */
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

    /**
     * to check the login credential
     */
    @SuppressLint("SuspiciousIndentation")
    fun checkLogin(mail: String, password: String): Boolean {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val isLoginUser = realm.where(UserRealmModel::class.java)
            .equalTo("mail", mail)
            .equalTo("password", password)
            .findFirst()
        realm.close()
        return isLoginUser != null
    }


    /**
     * location data into realm database
     */
    fun insertLocationDetails(locationData: LocationData) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealm(locationData)
        realm.commitTransaction()
        realm.close()
    }


    /**
     * to get the all location details from database
     */
    fun getAllLocationData(mail: String): ArrayList<LocationData> {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        // Use RealmQuery to retrieve all items of LocationData from the database
        val realmResults: RealmResults<LocationData> = realm.where(LocationData::class.java)
            .equalTo("email", mail)
            .findAll()
        // Convert RealmResults to a regular Kotlin List
        val locationData = ArrayList(realm.copyFromRealm(realmResults))
        Log.d("TAG", "getAllLocationData: ${realmResults.size}")
        realm.close()
        return locationData
    }


    /**
     * to get the all user from database
     */
    fun getAllUsers(): ArrayList<UserRealmModel> {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val allUser:RealmResults<UserRealmModel> = realm.where(UserRealmModel::class.java)
            .findAll()
        val users=ArrayList(realm.copyFromRealm(allUser))
        realm.close()
        return users
    }

}