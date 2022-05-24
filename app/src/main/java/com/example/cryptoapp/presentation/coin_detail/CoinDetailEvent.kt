package com.example.cryptoapp.presentation.coin_detail

import com.example.cryptoapp.presentation.coin_list.CoinListEvent

sealed class CoinDetailEvent{
    object Refresh: CoinDetailEvent()
    data class GetOhlc(
       val ohlcPereud: OhlcPereud
    ) : CoinDetailEvent()
}
