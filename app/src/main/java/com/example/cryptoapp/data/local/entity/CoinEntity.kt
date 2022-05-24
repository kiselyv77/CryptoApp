package com.example.cryptoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CoinEntity(
    @PrimaryKey val id: String,
    val isActive: Boolean,
    val isNew: Boolean,
    val name: String,
    val rank: Int,
    val symbol: String,
    val type: String,
)
