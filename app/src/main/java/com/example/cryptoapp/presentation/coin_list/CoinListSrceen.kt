package com.example.cryptoapp.presentation.coin_list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptoapp.presentation.coin_list.components.CoinItem
import com.example.cryptoapp.presentation.coin_list.components.SearchBar
import com.example.cryptoapp.presentation.coin_list.components.SearchState
import com.example.cryptoapp.presentation.coin_list.components.rememberSearchState
import com.example.cryptoapp.presentation.destinations.CoinDetailScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Destination(start = true)
fun CoinListScreen(
    navigator: DestinationsNavigator,
    viewModel: CoinListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )
    val lazyListState = rememberLazyListState()
    val searchState: SearchState<String, String> = rememberSearchState()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it){ data ->
                Snackbar(
                    modifier = Modifier.padding(10.dp),
                    contentColor = MaterialTheme.colors.error,
                    action = {
                        TextButton(onClick={ data.performAction() }){
                            Text(
                                text = "Повторить",
                            )
                        }
                    }
                ){
                    Text(
                        text = "Ошибка загрузки",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.error
                    )
                }
            }
        },
        topBar = {
            SearchBar(
                query = searchState.query,
                onQueryChange = { searchState.query = it
                    viewModel.onEvent(CoinListEvent.OnSearchQueryChange(it.text))},
                onSearchFocusChange = { searchState.focused = it },
                onClearQuery = { searchState.query = TextFieldValue("")
                    viewModel.onEvent(CoinListEvent.OnSearchQueryChange("")) },
                onBack = { searchState.query = TextFieldValue("")
                    viewModel.onEvent(CoinListEvent.OnSearchQueryChange(""))},
                searching = searchState.searching,
                focused = searchState.focused,
                //modifier = Modifier
            )
        },
        floatingActionButton = { FloatingActionButton(
            onClick = { coroutineScope.launch { lazyListState.scrollToItem(0) } },
            backgroundColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.9f)
        ) {
            Image(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
        }}
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    modifier = Modifier.padding(start = 64.dp, end = 32.dp),
                    text = "name",
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier.padding(end = 46.dp),
                    text = "graph 24h",
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier,
                    text = "price",
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Box(modifier = Modifier.fillMaxSize().padding(it)) {
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        viewModel.onEvent(CoinListEvent.Refresh)
                    }
                ){
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState
                    ) {
                        items(state.coins) { coin ->
                            CoinItem(
                                coin = coin,
                                onItemClick = {
                                    navigator.navigate(CoinDetailScreenDestination(coin.id))
                                },
                            )
                        }
                    }

                }

                if(state.isError) {
                    LaunchedEffect(scaffoldState.snackbarHostState) {
                        val result = scaffoldState.snackbarHostState.showSnackbar("")
                        if(result==SnackbarResult.ActionPerformed) viewModel.onEvent(CoinListEvent.Refresh)
                    }
                }
            }
        }

    }
    
}