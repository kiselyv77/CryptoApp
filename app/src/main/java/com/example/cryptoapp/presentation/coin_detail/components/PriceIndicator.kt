package com.example.cryptoapp.presentation.coin_detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun PriceIndicator(price:String, priceChangeTaday:Double){
    val priceChangeTadayRound = String.format("%.2f", priceChangeTaday)
    val colorTextChangeText = if (priceChangeTaday > 0) {
        Color.Green
    } else {
        Color.Red
    }
    val indicator = if (priceChangeTaday > 0) {
        Icons.Default.ArrowDropUp
    } else {
        Icons.Default.ArrowDropDown
    }
    Column() {
        Text(
            modifier = Modifier,
            text = "$${price}",
            style = MaterialTheme.typography.h6,
        )
        Row() {
            Text(
                modifier = Modifier,
                color = colorTextChangeText,
                text = "${priceChangeTadayRound}% today",
                style = MaterialTheme.typography.subtitle2,
            )
            Image(
                modifier = Modifier,
                imageVector = indicator,
                colorFilter = ColorFilter.tint(colorTextChangeText),
                contentDescription = null
            )

        }

    }
}

@Composable
fun PriceIndicatorPlaceholder(){
    Column() {
        Text(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .placeholder(
                    color = Color.Gray,
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(
                        highlightColor = Color.White,
                    )
                ),
            text = "100000",
            style = MaterialTheme.typography.h6,
        )
        Row() {
            Text(
                modifier = Modifier
                    .placeholder(
                        color = Color.Gray,
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,)
                    ),
                text = "0,000",
                style = MaterialTheme.typography.subtitle2,
            )

        }

    }
}