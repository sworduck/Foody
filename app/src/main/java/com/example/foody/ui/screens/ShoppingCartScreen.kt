package com.example.foody.ui.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foody.MainViewModel
import com.example.foody.data.models.products.ProductsItem
import com.example.foody.ui.FoodyScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun ShoppingCartScreen(
    viewModel: MainViewModel,
    navController: NavController,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    val productList = viewModel.filteredProductFlow.collectAsState().value.filter { it.count > 0 }
    val currentPrice = viewModel.currentCartPrice.collectAsState().value
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
                    .padding(16.dp),
                content = {
                    Text(text = "Заказать за $currentPrice ₽")
                })
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            itemsIndexed(productList) { _, item ->
                ShoppingCartItem(productsItem = item, viewModel::saveInCart)
                Divider()
            }
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShoppingCartItem(
    productsItem: ProductsItem,
    saveInCart: (Int, Int) -> Unit,
) {
    Row(Modifier.fillMaxWidth()) {
        GlideImage(
            modifier = Modifier.sizeIn(maxHeight = 100.dp, maxWidth = 100.dp),
            model = Uri.parse("file:///android_asset/1.png"),
            contentDescription = null
        )
        Column {
            Text(text = productsItem.name, Modifier.padding(start = 16.dp))
            Counter(
                changeCount = { newCount ->
                    saveInCart(productsItem.id, newCount)
                    //count.intValue = newCount
                },
                productsItem.count
            )
        }
        Column(Modifier.fillMaxWidth().align(alignment = Alignment.CenterVertically).padding(end = 16.dp)) {
            Text(
                text = productsItem.price_current.toString(),
                textAlign = TextAlign.Right,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth())
            if (productsItem.price_old != 0)
                Text(
                    textAlign = TextAlign.Right,
                    text = "${productsItem.price_old} ₽",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp).fillMaxWidth(),
                    style = TextStyle(textDecoration = TextDecoration.LineThrough),
                    color = Color.Gray,
                )
        }
    }
}