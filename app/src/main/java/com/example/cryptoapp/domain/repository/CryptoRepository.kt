package com.example.cryptoapp.domain.repository

import com.example.cryptoapp.data.local.entity.CoinDetailEntity
import com.example.cryptoapp.data.remote.dto.CoinDto
import com.example.cryptoapp.data.remote.dto.OhlcDto
import com.example.cryptoapp.data.remote.dto.coin_detail.CoinDetailDto
import com.example.cryptoapp.domain.model.CoinDetail

interface CryptoRepository {

    suspend fun getCoins(query:String, loadRemote: Boolean):List<CoinDto>

    suspend fun getOhlc(coinId:String, start:String, end:String):List<OhlcDto>

    suspend fun getOhlcToday(coinId:String):List<OhlcDto>

    suspend fun getOhlcLastFullDay(coinId:String):List<OhlcDto>

    suspend fun getCoinById(coinId:String, loadRemote: Boolean): CoinDetailEntity?
}