package com.example.cryptoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cryptoapp.data.local.entity.CoinDetailEntity
import com.example.cryptoapp.data.local.entity.CoinEntity
import javax.inject.Singleton


@Database(
    entities = [CoinEntity::class, CoinDetailEntity::class],
    version = 1
)
@TypeConverters(RoomConverters::class)
@Singleton
abstract class CryptoDataBase: RoomDatabase() {
    abstract val dao: CryptoDao
}