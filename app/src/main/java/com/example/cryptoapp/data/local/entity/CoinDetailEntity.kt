package com.example.cryptoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cryptoapp.data.local.RoomConverters

@Entity
data class CoinDetailEntity(
    @PrimaryKey val coinId:String,
    val name:String,
    val description:String,
    val symbol:String,
    val rank:Int,
    val isActive:Boolean,
    val tags:List<String>,
)
