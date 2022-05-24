package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.remote.dto.OhlcDto
import com.example.cryptoapp.domain.model.Ohlc

fun OhlcDto.toOhlc():Ohlc{
    return Ohlc(
        close = close,
        high = high,
        low = low,
        marketCap = marketCap,
        open = open,
        timeClose = timeClose,
        timeOpen = timeOpen,
        volume = volume

    )
}