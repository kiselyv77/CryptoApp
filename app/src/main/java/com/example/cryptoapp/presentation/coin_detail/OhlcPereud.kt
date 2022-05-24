package com.example.cryptoapp.presentation.coin_detail

sealed class OhlcPereud{
    object Month: OhlcPereud()
    object Day: OhlcPereud()
    object Week: OhlcPereud()
    object Year: OhlcPereud()
    object Full: OhlcPereud()
}
