package com.example.cryptoapp.presentation.coin_detail

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.cryptoapp.R
import com.example.cryptoapp.presentation.coin_detail.components.PriceIndicator
import com.example.cryptoapp.presentation.coin_detail.components.PriceIndicatorPlaceholder
import com.example.cryptoapp.presentation.coin_detail.consta.Constants.descriptionPlaceholder
import com.example.cryptoapp.presentation.common.components.line.SolidLineDrawer
import com.example.cryptoapp.presentation.destinations.CoinDetailScreenDestination
import com.example.cryptoapp.presentation.destinations.CoinListScreenDestination
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.cancel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Destination
fun CoinDetailScreen(
    id: String,
    navigator: DestinationsNavigator,
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )
    val scaffoldState = rememberScaffoldState()
    val labelRatioXState = remember{ mutableStateOf<Int>(9)}
    val offsetXState = remember{ mutableStateOf<Float>(1f)}
    val iconUrl = "https://static.coinpaprika.com/coin/${state.coin?.coinId}/logo.png?rev=10557311"

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    modifier = Modifier.padding(10.dp),
                    contentColor = MaterialTheme.colors.error,
                    action = {
                        TextButton(onClick = { data.performAction() }) {
                            Text(
                                text = "??????????????????",
                            )
                        }
                    }
                ) {
                    Text(
                        text = "???????????? ????????????????",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.error
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.popBackStack() },
                backgroundColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.9f)
            ) {
                Image(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.onEvent(CoinDetailEvent.Refresh) }
            ) {
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        Row(
                            modifier = Modifier.padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            val painter = rememberAsyncImagePainter(
                                iconUrl,
                            )

                            state.coin?.coinId?.also {
                                Image(
                                    painter = painter ,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                )
                            }?:run{
                                Image(
                                    imageVector = Icons.Default.Download ,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp).placeholder(
                                        color = Color.Gray,
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(
                                            highlightColor = Color.White,
                                        )
                                    ),
                                )
                            }


                            state.coin?.name?.also {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .padding(start = 16.dp)
                                        .wrapContentWidth(),
                                    text = it,
                                    style = MaterialTheme.typography.h4,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                            } ?:run{
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .padding(start = 16.dp)
                                        .wrapContentWidth()
                                        .placeholder(
                                            color = Color.Gray,
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(
                                                highlightColor = Color.White,
                                            )
                                        ),
                                    text = "NameName",
                                    style = MaterialTheme.typography.h4,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (state.ohlcToday.isNotEmpty()) {
                                val price = String.format("%.2f", state.ohlcToday.last().close)
                                val priceChangeTaday = state.ohlcToday.first().open.let {
                                    state.ohlcToday.last().close.div(
                                        it
                                    )
                                }.minus(1).times(100)
                                PriceIndicator(price = price, priceChangeTaday = priceChangeTaday)
                            }
                            else {
                                PriceIndicatorPlaceholder()
                            }
                        }
                        state.coin?.description?.also{
                            Text(
                                modifier = Modifier.padding(bottom = 16.dp),
                                text = it,
                                style = MaterialTheme.typography.h6,
                            )

                        }?:run{
                            Text(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .placeholder(
                                        color = Color.Gray,
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(
                                            highlightColor = Color.White,
                                        )
                                    ),
                                text = descriptionPlaceholder,
                                style = MaterialTheme.typography.h6,
                            )
                        }


                        val dataList = arrayListOf<LineChartData.Point>()
                        if (!state.isLoading && !state.isError) {
                            state.ohlcHistorical.forEach{
                                dataList.add(LineChartData.Point(it.close.toFloat(), it.timeClose.dropLast(10)))
                            }

                            LineChart(
                                lineChartData = LineChartData(points = dataList),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(16.dp)
                                    .pointerInput(Unit) {
                                        detectTransformGestures { _, _, zoom, _ ->
                                            val addOffset = (zoom - 1) * 100 * -1
                                            if (!(addOffset > 0 && offsetXState.value > 30)) {
                                                offsetXState.value += addOffset
                                                //if(addOffset<0) labelRatioXState.value -= 1 else labelRatioXState.value+=1

                                            }
                                            Log.d("CoinDetailScreen", "${offsetXState.value}")
                                        }
                                    },
                                xAxisDrawer = SimpleXAxisDrawer(
                                    axisLineColor = Color.White,
                                    labelTextColor = Color.White,
                                    labelRatio = labelRatioXState.value
                                ),
                                yAxisDrawer = SimpleYAxisDrawer(
                                    axisLineColor = Color.White,
                                    labelTextColor = Color.White,
                                ),
                                lineDrawer = SolidLineDrawer(color = MaterialTheme.colors.onPrimary),
                                horizontalOffset =  offsetXState.value,
                            )
                        }
                        else{
                            LineChart(
                                lineChartData = LineChartData(points = dataList),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(16.dp),
                                xAxisDrawer = SimpleXAxisDrawer(
                                    axisLineColor = Color.White,
                                    labelTextColor = Color.White,
                                    labelRatio = 9
                                ),
                                yAxisDrawer = SimpleYAxisDrawer(
                                    axisLineColor = Color.White,
                                    labelTextColor = Color.White,
                                ),
                                lineDrawer = SolidLineDrawer(color = MaterialTheme.colors.onPrimary),
                                horizontalOffset = 1f,
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            OutlinedButton(
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.onPrimary,
                                    contentColor = Color.White
                                ),
                                border = ButtonDefaults.outlinedBorder
                                    .copy(
                                        brush = Brush.horizontalGradient(
                                            listOf(Color.White,
                                                Color.White,)
                                        )
                                    ).takeIf { viewModel.state.value.ohlcPereud == OhlcPereud.Week },
                                onClick = {
                                    viewModel.onEvent(CoinDetailEvent.GetOhlc(OhlcPereud.Week))
                                    labelRatioXState.value = 2
                                }
                            ) {
                                Text("Week")
                            }
                            OutlinedButton(
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.onPrimary,
                                    contentColor = Color.White
                                ),
                                border = ButtonDefaults.outlinedBorder
                                    .copy(
                                        brush = Brush.horizontalGradient(
                                            listOf(Color.White,
                                                Color.White,)
                                        )
                                    ).takeIf { viewModel.state.value.ohlcPereud == OhlcPereud.Month },
                                onClick = {
                                    viewModel.onEvent(CoinDetailEvent.GetOhlc(OhlcPereud.Month))
                                    labelRatioXState.value = 9
                                }
                            ) {
                                Text("Month")
                            }
                            OutlinedButton(
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.onPrimary,
                                    contentColor = Color.White
                                ),
                                border = ButtonDefaults.outlinedBorder
                                    .copy(
                                        brush = Brush.horizontalGradient(
                                            listOf(Color.White,
                                                Color.White,)
                                        )
                                    ).takeIf { viewModel.state.value.ohlcPereud == OhlcPereud.Year },
                                onClick = {
                                    viewModel.onEvent(CoinDetailEvent.GetOhlc(OhlcPereud.Year))
                                    labelRatioXState.value = 110
                                }
                            ) {
                                Text("Year")
                            }

                        }
                    }
                }

            }
            if (state.isError) {
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    val result = scaffoldState.snackbarHostState.showSnackbar("")
                    if (result == SnackbarResult.ActionPerformed) viewModel.onEvent(CoinDetailEvent.Refresh)
                }
            }
        }
    }

}

