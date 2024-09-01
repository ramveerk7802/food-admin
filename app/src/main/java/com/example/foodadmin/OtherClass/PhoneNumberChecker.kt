package com.example.foodadmin.OtherClass

class PhoneNumberChecker {
     fun phoneNumberValidation(number:String):Boolean{
        if(number.length<10 || number.length>10)
            return false
        else{
            for(element in number){
                if(!isDigit(element))
                    return false
            }
        }
        return true
    }
     private fun isDigit(ch:Char):Boolean{
        return (ch >= '0') && (ch <= '9')
    }
}