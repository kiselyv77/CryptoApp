package com.example.cryptoapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomConverters {
    @TypeConverter
    fun saveDoubleList(listOfString: List<String>): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun getDoubleList(listOfString: String): List<String> {
        return Gson().fromJson(
            listOfString.toString(),
            object : TypeToken<List<String?>?>() {}.type
        )
    }
}