package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable

class Token : Parcelable
{
    var type : TokenType
    var value : String

    constructor(type : TokenType, value : String)
    {
        this.type = type
        this.value = value
    }

    constructor(parcel : Parcel)
    {
        val data : Array<String?> = arrayOfNulls(2)
        parcel.readStringArray(data)
        this.type = TokenType.valueOf(data[0]!!)
        this.value = data[1]!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeStringArray(arrayOf(type.toString(), value))
    }

    override fun describeContents(): Int
    {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Token>
    {
        override fun createFromParcel(parcel: Parcel): Token
        {
            return Token(parcel)
        }

        override fun newArray(size: Int): Array<Token?> {
            return arrayOfNulls(size)
        }
    }

}

enum class TokenType
{
    NUMBER, OPERATION, OPEN_BRACKET, CLOSE_BRACKET
}
