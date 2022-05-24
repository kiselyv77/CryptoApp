package com.example.cryptoapp.presentation.coin_list

import com.example.cryptoapp.domain.model.Coin
import com.example.cryptoapp.domain.model.Ohlc

data class CoinListState(
    val coins: List<Coin> = emptyList(),
    var ohlcs: MutableMap<String, List<Ohlc>> = mutableMapOf(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val isError: Boolean = false
)
