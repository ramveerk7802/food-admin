package com.example.foodadmin.DataModel

import android.health.connect.datatypes.ExerciseRoute.Location
import android.provider.ContactsContract.CommonDataKinds.Email

data class UserModel(
    val ownerName:String? = null,
    val address:String ?=null,
    val location: String ?=null,
    val restaurantName:String? = null,
    val email: String? = null,
    val phone:String? = null,
    val password:String? = null,
    val userId : String? = null

)
