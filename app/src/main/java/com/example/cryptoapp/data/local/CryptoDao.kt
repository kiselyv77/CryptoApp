package com.example.cryptoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptoapp.data.local.entity.CoinDetailEntity
import com.example.cryptoapp.data.local.entity.CoinEntity

@Dao
interface CryptoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinList(
        companyListingEntities: List<CoinEntity>
    )

    @Query("DELETE FROM coinentity")
    suspend fun clearCoinList()


    @Query(
        """
            SELECT * 
            FROM coinentity 
            WHERE LOWER(name)
            LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == symbol
            """
    )
    suspend fun searchCoin(query: String): List<CoinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinDetail(
        coinDetailEntity: CoinDetailEntity
    )

    @Query("DELETE FROM coindetailentity")
    suspend fun deleteCoinDetail()

    @Query("SELECT * FROM coindetailentity WHERE coinId=:coinId ")
    suspend fun getCoinDetail(coinId:String): CoinDetailEntity?

}