package com.example.foody.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foody.MainViewModel
import com.example.foody.ui.FoodyScreen

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CardProductScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    id: Int = 1,
) {

    val product = viewModel.productFlow.collectAsState().value.first { it.id == id }
    Scaffold(
        bottomBar = {
            FoodyButton(
                onClick = {
                    viewModel.saveInCart(id, product.count + 1)
                    navController.navigate(FoodyScreen.ShoppingCart.name)
                },
                modifier = Modifier
                    .padding(16.dp, 12.dp)
                    .fillMaxWidth(),
                content = {
                    Text(text = "В корзину за ${product.price_current} ₽")
                })
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                GlideImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = Uri.parse("file:///android_asset/1.png"),
                    contentDescription = null
                )
            }
            item {
                Text(text = "${product.name}", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp,24.dp))
            }
            item {
                Text(text = "${product.description}",Modifier.padding(16.dp))
            }

            itemsIndexed(
                listOf(
                    Pair("Вес", "${product.measure} ${product.measure_unit}"),
                    Pair("Энергетическая ценность", "${product.energy_per_100_grams}"),
                    Pair("Белки", "${product.proteins_per_100_grams}"),
                    Pair("Жиры", "${product.fats_per_100_grams}"),
                    Pair("Углеводы", "${product.carbohydrates_per_100_grams}"),
                )
            ) { _, item ->
                CardProductItem(propertyName = item.first, properties = item.second)
            }
        }
    }
    IconButton(
        onClick = {
            navController.navigateUp()
        },
        Modifier
            .padding(16.dp)
            .shadow(2.dp, CircleShape)
            .background(color = Color.White, CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = ""
        )
    }
}

@Composable
fun CardProductItem(
    propertyName: String,
    properties: String,
) {
    Divider()
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp,13.dp)) {
        Text(text = "$propertyName")
        Text(text = "$properties")
    }
}