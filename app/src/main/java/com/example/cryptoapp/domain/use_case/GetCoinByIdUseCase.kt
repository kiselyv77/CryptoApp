package com.example.cryptoapp.domain.use_case

import com.example.cryptoapp.data.mapper.toCoin
import com.example.cryptoapp.data.mapper.toCoinDetail
import com.example.cryptoapp.domain.model.Coin
import com.example.cryptoapp.domain.model.CoinDetail
import com.example.cryptoapp.domain.repository.CryptoRepository
import com.example.cryptoapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinByIdUseCase @Inject constructor(
    private val repository: CryptoRepository
) {

    operator fun invoke(coinId:String, loadRemote:Boolean): Flow<Resource<CoinDetail>> = flow{
        try{
            emit(Resource.Loading<CoinDetail>())
            val coin = repository.getCoinById(coinId, loadRemote)?.toCoinDetail()
            emit(Resource.Success<CoinDetail>(coin))
        } catch (e: HttpException){
            emit(Resource.Error<CoinDetail>(e.localizedMessage?: "unexpected error"))
        }catch (e: IOException){
            emit(Resource.Error<CoinDetail>(e.localizedMessage?: "not enternet"))
        }

    }
}