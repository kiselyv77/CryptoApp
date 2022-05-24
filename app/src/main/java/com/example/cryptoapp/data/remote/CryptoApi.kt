package com.example.cryptoapp.data.remote

import com.example.cryptoapp.data.remote.dto.CoinDto
import com.example.cryptoapp.data.remote.dto.OhlcDto
import com.example.cryptoapp.data.remote.dto.coin_detail.CoinDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoApi {

    @GET("/v1/coins")
    suspend fun getCoins(): List<CoinDto>


    @GET("/v1/coins/{coinId}/ohlcv/historical?")
    suspend fun getOhlc(
        @Path("coinId") coinId: String,
        @Query("start") start:String,
        @Query("end") end:String
    ): List<OhlcDto>


    @GET("/v1/coins/{coinId}/ohlcv/today")
    suspend fun getOhlcToday(
        @Path("coinId") coinId: String,
    ): List<OhlcDto>

    @GET("/v1/coins/{coinId}/ohlcv/latest")
    suspend fun getOhlcLastFullDay(
        @Path("coinId") coinId: String,
    ): List<OhlcDto>


    @GET("/v1/coins/{coinId}")
    suspend fun getCoinById(@Path("coinId") coinId: String): CoinDetailDto?

}