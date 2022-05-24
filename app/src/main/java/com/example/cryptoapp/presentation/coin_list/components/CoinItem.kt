package com.example.cryptoapp.presentation.coin_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.LocalImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.example.cryptoapp.domain.model.Coin
import com.example.cryptoapp.presentation.coin_list.CoinListEvent
import com.example.cryptoapp.presentation.coin_list.CoinListViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.shimmer
import com.google.accompanist.placeholder.material.placeholder

@Composable
fun CoinItem(
    coin: Coin,
    viewModel: CoinListViewModel = hiltViewModel(),
    onItemClick: (Coin) -> Unit,

) {
    val state = viewModel.state.value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(coin) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically

    ) {
        val iconUrl = "https://static.coinpaprika.com/coin/${coin.id}/logo.png?rev=10557311"
        val graphic7day = "https://graphs.coinpaprika.com/currency/chart/${coin.id}/24h/chart.svg"

        Column(
            Modifier.padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = rememberAsyncImagePainter(iconUrl),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )

            Text(
                text = "${coin.rank}",
                style = MaterialTheme.typography.overline,
                overflow = TextOverflow.Ellipsis
            )

        }

        Column(
            modifier = Modifier.padding(end = 16.dp)
        ){
            Text(
                text = coin.symbol,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = coin.name,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
            )

        }

        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()


        CompositionLocalProvider(LocalImageLoader provides imageLoader) {
            Image(
                painter = rememberAsyncImagePainter(graphic7day),
                contentDescription = "SVG Image",

                modifier = Modifier
                    .height(32.dp)
                    .padding(end = 16.dp)
                    .fillMaxWidth(0.4F),
            )
        }

        //viewModel.onEvent(CoinListEvent.GetOhlc(coin.id))

        val priceChangeTaday = state.ohlcs[coin.id]?.first()?.close?.let {
            state.ohlcs[coin.id]?.last()?.close?.div(
                it
            )
        }?.minus(1)?.times(100)
        val priceChangeTadayRound = String.format("%.2f", priceChangeTaday)
        val price = state.ohlcs[coin.id]?.last()?.close
        val priceRound = String.format("%.2f", price)

        if (price != null){
            Column() {
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = "$${priceRound}",
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                val colorTextChangeText = if(priceChangeTaday!! > 0){Color.Green} else{Color.Red}
                val indicator = if(priceChangeTaday > 0){Icons.Default.ArrowDropUp} else{Icons.Default.ArrowDropDown}
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier,
                        text = "${priceChangeTadayRound}%",
                        style = MaterialTheme.typography.caption,
                        color = colorTextChangeText,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Image( modifier = Modifier, imageVector = indicator, colorFilter = ColorFilter.tint(colorTextChangeText), contentDescription = null)
                }
            }
        }
        else{
            Column() {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp).width(80.dp)
                        .placeholder(color = Color.Gray, visible = true ,highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White,)),
                    text = "",
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier.width(60.dp)
                        .placeholder(color = Color.Gray, visible = true ,highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White,)),
                    text = "",
                    style = MaterialTheme.typography.caption,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

