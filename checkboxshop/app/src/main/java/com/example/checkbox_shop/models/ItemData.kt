package com.example.checkbox_shop.models

import android.os.Parcel
import android.os.Parcelable

class ItemData(var id: Int, var name: String, var check: Boolean) : Parcelable
{
    //Constructor for item creation from parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readBoolean()
    )

    override fun describeContents(): Int {
        return 0
    }

    //Pack object into parcel
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeBoolean(check)
    }

    //Creator object is used for item creation and filling it with data from parcel
    companion object CREATOR : Parcelable.Creator<ItemData> {
        //Retrieve item from parcel
        override fun createFromParcel(parcel: Parcel): ItemData {
            return ItemData(parcel)
        }

        override fun newArray(size: Int): Array<ItemData?> {
            return arrayOfNulls(size)
        }
    }

}
