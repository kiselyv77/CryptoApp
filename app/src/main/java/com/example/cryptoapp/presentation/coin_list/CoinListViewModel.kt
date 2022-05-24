package com.example.cryptoapp.presentation.coin_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.domain.use_case.GetCoinsUseCase
import com.example.cryptoapp.domain.use_case.GetOhlcUseCase
import com.example.cryptoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getOhlcUseCase: GetOhlcUseCase
): ViewModel() {

    private val _state = mutableStateOf(CoinListState())
    val state: State<CoinListState> = _state

    init{
        getCoinList()
    }

    fun onEvent(event: CoinListEvent){
       when(event){
           is CoinListEvent.Refresh -> {
               getCoinList(loadRemote = true)
           }
           is CoinListEvent.OnSearchQueryChange -> {
               _state.value = _state.value.copy(searchQuery = event.query)
               getCoinList()
           }
           is CoinListEvent.GetOhlc -> {
               getCoinOhlc(event.coinId)
           }
       }
    }

    private fun getCoinList(
        query: String = _state.value.searchQuery.lowercase(),
        loadRemote: Boolean = false){

        viewModelScope.launch {
            getCoinsUseCase(query, loadRemote)
                .collect{ result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let { coins ->
                                _state.value = _state.value.copy(
                                    coins = coins,
                                    isLoading = false
                                )
                            }
                        }
                        is Resource.Error ->{
                            _state.value = _state.value.copy(isError = true, isLoading = false)
                            Log.d("view_model_error", result.message.toString())
                        }
                        is Resource.Loading ->{
                            _state.value = _state.value.copy(isLoading =  result.isLoading, isError = false)
                        }
                    }

                }
        }
    }

    private fun getCoinOhlc(coinId: String) {
        val dateOpen = ZonedDateTime.now().minusHours(24L)

        val dateClose = ZonedDateTime.now()

        val dataOpenString = DateTimeFormatter.ISO_INSTANT.format(dateOpen)
        val dataCloseString = DateTimeFormatter.ISO_INSTANT.format(dateClose)



        Log.d("view_model2", "$dataOpenString $dataCloseString")
        viewModelScope.launch {
            getOhlcUseCase.getOhlc(coinId, dataOpenString, dataCloseString)
                .collect{ result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let { ohlc ->
                                val original = _state.value.ohlcs //Ссылка на оригинальную карту
                                val copy = HashMap(original) // Копируем карту
                                copy[coinId] = ohlc // Присваиваем полученное с апи значение
                                /* Только при копировании обьекта состояния происходит  recompose()*/
                                _state.value = _state.value.copy(ohlcs = copy)
                            }
                        }
                        is Resource.Error ->{
                            Log.d("view_model_error", result.message.toString())
                        }
                        is Resource.Loading ->{
                            //_state.value = _state.value.copy(isLoading =  result.isLoading, isError = false)
                        }
                    }

                }
        }
    }

}
