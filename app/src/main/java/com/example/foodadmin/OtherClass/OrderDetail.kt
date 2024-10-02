package com.example.foodadmin.OtherClass

import android.os.Parcel
import android.os.Parcelable

class OrderDetail():Parcelable{
    var uid:String?=null
    var name:String?=null
    var address:String?=null
    var mobile:String?=null
    var totalAmt:Int=0
    var itemName:MutableList<String>?=null
    var itemImage:MutableList<String>?=null
    var itemQuantity:MutableList<Int>?=null
    var itemPrice:MutableList<Int>?=null
    var currentDateAndTime:Long?=0
    var orderAccepted:Boolean=false
    var paymentReceived:Boolean=false
    var orderKey:String?=null

    constructor(parcel: Parcel) : this() {
        uid = parcel.readString()
        name = parcel.readString()
        address = parcel.readString()
        mobile = parcel.readString()
        totalAmt = parcel.readInt()
        currentDateAndTime = parcel.readValue(Long::class.java.classLoader) as? Long
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        orderKey = parcel.readString()
    }

    constructor(
        userId: String,
        name: String,
        address: String,
        number: String,
        amount: Int,
        itemName: MutableList<String>,
        itemImage: MutableList<String>,
        itemQuantity: MutableList<Int>,
        itemPrice: MutableList<Int>,
        currentDateAndTime: Long?,
        orderAccepted: Boolean,
        paymentReceived: Boolean,
        orderKey: String
    ) : this(){
        this.uid= userId
        this.name=name
        this.address=address
        this.mobile=number
        this.totalAmt=amount
        this.itemName=itemName
        this.itemImage=itemImage
        this.itemQuantity=itemQuantity
        this.itemPrice=itemPrice
        this.currentDateAndTime=currentDateAndTime
        this.orderAccepted=orderAccepted
        this.paymentReceived=paymentReceived
        this.orderKey=orderKey
    }




    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(mobile)
        parcel.writeInt(totalAmt)
        parcel.writeValue(currentDateAndTime)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(orderKey)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetail> {
        override fun createFromParcel(parcel: Parcel): OrderDetail {
            return OrderDetail(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetail?> {
            return arrayOfNulls(size)
        }
    }


}
