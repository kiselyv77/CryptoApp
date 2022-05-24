package com.example.cryptoapp.domain.use_case

import com.example.cryptoapp.data.mapper.toCoin
import com.example.cryptoapp.data.mapper.toOhlc
import com.example.cryptoapp.domain.model.Coin
import com.example.cryptoapp.domain.model.Ohlc
import com.example.cryptoapp.domain.repository.CryptoRepository
import com.example.cryptoapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetOhlcUseCase @Inject constructor(
    private val repository: CryptoRepository
) {

    fun getOhlc(coinId:String, start:String, end:String): Flow<Resource<List<Ohlc>>> = flow{
        try{
            emit(Resource.Loading<List<Ohlc>>())
            val coins = repository.getOhlc(coinId, start, end).map{ it.toOhlc() }
            emit(Resource.Success<List<Ohlc>>(coins))
        } catch (e: HttpException){
            emit(Resource.Error<List<Ohlc>>(e.localizedMessage?: "unexpected error"))
        }catch (e: IOException){
            emit(Resource.Error<List<Ohlc>>(e.localizedMessage?: "not enternet"))
        }

    }

    fun getOhlcToday(coinId:String): Flow<Resource<List<Ohlc>>> = flow{
        try{
            emit(Resource.Loading<List<Ohlc>>())
            val coins = repository.getOhlcToday(coinId).map{ it.toOhlc() }
            emit(Resource.Success<List<Ohlc>>(coins))
        } catch (e: HttpException){
            emit(Resource.Error<List<Ohlc>>(e.localizedMessage?: "unexpected error"))
        }catch (e: IOException){
            emit(Resource.Error<List<Ohlc>>(e.localizedMessage?: "not enternet"))
        }

    }

    fun getOhlcLastFullDay(coinId:String): Flow<Resource<List<Ohlc>>> = flow{
        try{
            emit(Resource.Loading<List<Ohlc>>())
            val coins = repository.getOhlcLastFullDay(coinId).map{ it.toOhlc() }
            emit(Resource.Success<List<Ohlc>>(coins))
        } catch (e: HttpException){
            emit(Resource.Error<List<Ohlc>>(e.localizedMessage?: "unexpected error"))
        }catch (e: IOException){
            emit(Resource.Error<List<Ohlc>>(e.localizedMessage?: "not enternet"))
        }

    }


}