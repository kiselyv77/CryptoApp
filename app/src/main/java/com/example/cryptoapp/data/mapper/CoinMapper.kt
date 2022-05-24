package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.local.entity.CoinEntity
import com.example.cryptoapp.data.remote.dto.CoinDto
import com.example.cryptoapp.domain.model.Coin

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        isActive = isActive,
        isNew = isNew,
        name = name,
        rank = rank,
        symbol = symbol,
        type = type,
    )
}

fun CoinDto.toCoinEntity(): CoinEntity {
    return CoinEntity(
        id = id,
        isActive = isActive,
        isNew = isNew,
        name = name,
        rank = rank,
        symbol = symbol,
        type = type
    )
}

fun CoinEntity.toCoinDto(): CoinDto {
    return CoinDto(
        id = id,
        isActive = isActive,
        isNew = isNew,
        name = name,
        rank = rank,
        symbol = symbol,
        type = type
    )
}

