package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.local.CryptoDataBase
import com.example.cryptoapp.data.local.entity.CoinDetailEntity
import com.example.cryptoapp.data.mapper.*
import com.example.cryptoapp.data.remote.CryptoApi
import com.example.cryptoapp.data.remote.dto.CoinDto
import com.example.cryptoapp.data.remote.dto.OhlcDto
import com.example.cryptoapp.data.remote.dto.coin_detail.CoinDetailDto
import com.example.cryptoapp.domain.model.CoinDetail
import com.example.cryptoapp.domain.repository.CryptoRepository
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val api: CryptoApi,
    private val db: CryptoDataBase
): CryptoRepository  {
    private val dao = db.dao

    override suspend fun getCoins(query: String, loadRemote: Boolean): List<CoinDto> {
        val localCoins = dao.searchCoin("")

        return if(loadRemote || localCoins.isEmpty()){
            val remoteCoins = api.getCoins()
            dao.clearCoinList()
            dao.insertCoinList(remoteCoins.map{ it.toCoinEntity() })
            dao.searchCoin(query).map { it.toCoinDto() }
        } else{
            dao.searchCoin(query).map { it.toCoinDto() }
        }
    }

    override suspend fun getOhlc(coinId: String, start: String, end: String): List<OhlcDto> {
        return api.getOhlc(coinId, start, end)
    }

    override suspend fun getOhlcToday(coinId: String): List<OhlcDto> {
       return  api.getOhlcToday(coinId)
    }

    override suspend fun getOhlcLastFullDay(coinId: String): List<OhlcDto> {
        return api.getOhlcLastFullDay(coinId)
    }

    override suspend fun getCoinById(coinId: String, loadRemote: Boolean): CoinDetailEntity? {
        val localCoin = dao.getCoinDetail(coinId)
        return if(loadRemote || localCoin == null){
            val remoteCoin = api.getCoinById(coinId)

            dao.insertCoinDetail(remoteCoin.toCoinDetailEntity())

            dao.getCoinDetail(coinId)
        }
        else{
            dao.getCoinDetail(coinId)
        }
    }
}