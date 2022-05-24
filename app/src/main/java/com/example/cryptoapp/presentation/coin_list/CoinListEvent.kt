package com.example.cryptoapp.presentation.coin_list

sealed class CoinListEvent{
    object Refresh: CoinListEvent()
    data class GetOhlc(val coinId: String):CoinListEvent()
    data class OnSearchQueryChange(val query: String) :CoinListEvent()

}
