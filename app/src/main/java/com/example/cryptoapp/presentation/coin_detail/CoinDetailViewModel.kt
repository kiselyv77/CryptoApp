package com.example.cryptoapp.presentation.coin_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.domain.use_case.GetCoinByIdUseCase
import com.example.cryptoapp.domain.use_case.GetOhlcUseCase
import com.example.cryptoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getOhlcUseCase: GetOhlcUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val getCoinByIdUseCase: GetCoinByIdUseCase
): ViewModel() {

    private val _state = mutableStateOf(CoinDetailState())
    val state: State<CoinDetailState> = _state

    init{
        getCoinOhlcToday()
        getCoinOhlcHistorical(OhlcPereud.Month)
        getCoinById()
    }

    fun onEvent(event:CoinDetailEvent){
        when (event){
            is CoinDetailEvent.Refresh -> {
                getCoinById(true)
            }
            is CoinDetailEvent.GetOhlc -> {
                getCoinOhlcHistorical(event.ohlcPereud)
            }
        }

    }


    private fun getCoinOhlcToday() {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            getOhlcUseCase.getOhlcToday(id)
                .collect{ result ->
                    when(result){
                        is Resource.Success ->{

                            result.data?.let { ohlc ->
                                _state.value = _state.value.copy(
                                    ohlcToday = ohlc,
                                    isLoading = false
                                )
                            }
                        }
                        is Resource.Error ->{
                            _state.value = _state.value.copy(isError = true, isLoading = false)
                        }
                        is Resource.Loading ->{
                            _state.value = _state.value.copy(isLoading =  result.isLoading, isError = false)
                        }
                    }

                }
        }
    }

    private fun getCoinOhlcHistorical(ohlcPereud: OhlcPereud) {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            var dateOpen = ZonedDateTime.now()
            _state.value = _state.value.copy(ohlcPereud = ohlcPereud)
            when (ohlcPereud){
                is OhlcPereud.Week ->{
                    dateOpen = dateOpen.minusDays(7L)
                }
                is OhlcPereud.Month ->{
                    dateOpen = dateOpen.minusDays(30L)
                }
                is OhlcPereud.Year -> {
                    dateOpen = dateOpen.minusDays(365L)
                }
            }

            val dateClose = ZonedDateTime.now()

            val dataOpenString = DateTimeFormatter.ISO_INSTANT.format(dateOpen)
            val dataCloseString = DateTimeFormatter.ISO_INSTANT.format(dateClose)
            getOhlcUseCase.getOhlc(id, dataOpenString, dataCloseString)
                .collect{ result ->
                    when(result){
                        is Resource.Success ->{

                            result.data?.let { ohlc ->
                                _state.value = _state.value.copy(
                                    ohlcHistorical = ohlc,
                                    isLoading = false
                                )
                            }
                        }
                        is Resource.Error ->{
                            _state.value = _state.value.copy(isError = true, isLoading = false)
                        }
                        is Resource.Loading ->{
                            _state.value = _state.value.copy(isLoading =  result.isLoading, isError = false)
                        }
                    }

                }
        }
    }
    private fun getCoinById(loadRemote:Boolean = false){
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            getCoinByIdUseCase(id, loadRemote).collect{ result ->

                when(result){
                    is Resource.Success ->{

                        result.data?.let { coinDetail ->
                            _state.value = _state.value.copy(
                                coin = coinDetail,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error ->{
                        _state.value = _state.value.copy(isError = true, isLoading = false)
                    }
                    is Resource.Loading ->{
                        _state.value = _state.value.copy(isLoading =  result.isLoading, isError = false)
                    }
                }


            }

        }

    }
}