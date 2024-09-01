package com.example.foodadmin.OtherClass

import org.mindrot.jbcrypt.BCrypt

class PasswordEncodeManager {
     fun encode(password:String):String{
        return BCrypt.hashpw(password,BCrypt.gensalt())
    }
     fun matches(password: String,hashedPassword:String):Boolean{
        return BCrypt.checkpw(password,hashedPassword)
    }
}