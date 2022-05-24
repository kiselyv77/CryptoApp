package com.example.cryptoapp.presentation.coin_detail

import com.example.cryptoapp.domain.model.CoinDetail
import com.example.cryptoapp.domain.model.Ohlc

data class CoinDetailState(
    val coin: CoinDetail? = null,
    val ohlcHistorical: List<Ohlc> = emptyList(),
    val ohlcToday:List<Ohlc> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val ohlcPereud:OhlcPereud = OhlcPereud.Month
)
