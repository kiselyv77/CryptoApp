package com.example.cryptoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OhlcDto(
    val close: Double,
    val high: Double,
    val low: Double,
    @SerializedName("market_cap")
    val marketCap: Long,
    val open: Double,
    @SerializedName("time_close")
    val timeClose: String,
    @SerializedName("time_open")
    val timeOpen: String,
    val volume: Double,
)