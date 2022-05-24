package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.local.entity.CoinDetailEntity
import com.example.cryptoapp.data.remote.dto.coin_detail.CoinDetailDto
import com.example.cryptoapp.domain.model.CoinDetail

fun CoinDetailDto.toCoinDetail(): CoinDetail {
    return CoinDetail(
        coinId = id,
        name = name,
        description = description,
        symbol = symbol,
        rank= rank,
        isActive= is_active,
        tags =  tags.map{it.name},
    )
}

fun CoinDetailDto?.toCoinDetailEntity(): CoinDetailEntity {
    return CoinDetailEntity(
        coinId = this?.id ?: "",
        name = this?.name ?: "",
        description = this?.description ?: "",
        symbol = this?.symbol ?: "",
        rank= this?.rank ?: 0,
        isActive= this?.is_active ?: false,
        tags = this?.tags?.map{it.name} ?: emptyList(),
    )
}

fun CoinDetailEntity.toCoinDetail(): CoinDetail {
    return CoinDetail(
        coinId = coinId,
        name = name,
        description = description,
        symbol = symbol,
        rank= rank,
        isActive= isActive,
        tags =  tags,
    )
}

