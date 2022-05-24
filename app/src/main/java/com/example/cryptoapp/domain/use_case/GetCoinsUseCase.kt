package com.example.cryptoapp.domain.use_case

import com.example.cryptoapp.data.mapper.toCoin
import com.example.cryptoapp.domain.model.Coin
import com.example.cryptoapp.domain.repository.CryptoRepository
import com.example.cryptoapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CryptoRepository
) {

    operator fun invoke(query:String, loadRemote:Boolean): Flow<Resource<List<Coin>>> = flow{
        try{
            emit(Resource.Loading<List<Coin>>())
            val coins = repository.getCoins(query, loadRemote).map{ it.toCoin() }
            emit(Resource.Success<List<Coin>>(coins))
        } catch (e: HttpException){
            emit(Resource.Error<List<Coin>>(e.localizedMessage?: "unexpected error"))
        }catch (e: IOException){
            emit(Resource.Error<List<Coin>>(e.localizedMessage?: "not enternet"))
        }

    }

}