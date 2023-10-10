package com.example.foody.ui.screens

import android.net.Uri
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foody.MainViewModel
import com.example.foody.R
import com.example.foody.data.models.products.ProductsItem
import com.example.foody.ui.components.Counter
import com.example.foody.ui.components.FoodyButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingCartScreen(
    viewModel: MainViewModel,
    newProducts: String
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    val uiState = viewModel.catalogUiState.collectAsState()
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        bottomBar = {
            FoodyButton(
                onClick = {
                    localCoroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = "Заказ отправлен на предприятие",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                content = {
                    Text(text = "Заказать за ${uiState.value.cartPrice} ₽", fontSize = 16.sp)
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            itemsIndexed(
                uiState.value.productList.filter { it.count > 0 },
                key = { _, item -> item.id }) { _, item ->
                ShoppingCartItem(
                    productsItem = item,
                    saveInCart = viewModel::saveInCart,
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth(),
                    recentlyAdded = item.id == newProducts.toInt()
                )
            }
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShoppingCartItem(
    modifier: Modifier,
    productsItem: ProductsItem,
    saveInCart: (Int, Int) -> Unit,
    recentlyAdded: Boolean
) {
    val color = remember { Animatable(Color.White) }
    LaunchedEffect(Unit) {
        if(recentlyAdded) {
            color.animateTo(Color.Green.copy(alpha = 0.3f), animationSpec = tween(1000))
            color.animateTo(Color.White, animationSpec = tween(1000))
        }
    }
    Column(modifier.background(color.value)) {
        Row {
            GlideImage(
                modifier = Modifier.sizeIn(maxHeight = 100.dp, maxWidth = 100.dp),
                model = Uri.parse(stringResource(id = R.string.picture_url)),
                contentDescription = null
            )
            Column(
                Modifier
                    .weight(0.5f)
            ) {
                Text(text = productsItem.name, Modifier.padding(start = 16.dp))
                Counter(
                    modifier = Modifier
                        .padding(8.dp),
                    changeCount = { newCount ->
                        saveInCart(productsItem.id, newCount)
                    },
                    count = productsItem.count,
                    buttonBackGroundColor = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            Column(
                Modifier
                    .weight(0.3f)
                    .align(alignment = Alignment.CenterVertically)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = productsItem.price_current.toString(),
                    textAlign = TextAlign.Right,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                if (productsItem.price_old != 0)
                    Text(
                        textAlign = TextAlign.Right,
                        text = "${productsItem.price_old} ₽",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .fillMaxWidth(),
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        color = Color.Gray,
                    )
            }
        }
        Divider()
    }
}

@Preview
@Composable
fun ShoppingCartItemPreview() {
    ShoppingCartItem(
        productsItem = ProductsItem(
            id = 1,
            name = "Картофель",
            measure = 1,
            measure_unit = "кг",
            price_current = 100,
            price_old = 100,
            count = 1,
            carbohydrates_per_100_grams = 100.0,
            fats_per_100_grams = 100.0,
            proteins_per_100_grams = 100.0,
            energy_per_100_grams = 100.0,
            category_id = 1,
            image = stringResource(id = R.string.picture_url),
            description = "",
            tag_ids = listOf(1)
        ),
        saveInCart = { _: Int, _: Int -> },
        modifier = Modifier,
        recentlyAdded = false
    )
}