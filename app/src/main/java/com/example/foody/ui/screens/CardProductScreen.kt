package com.example.foody.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foody.MainViewModel
import com.example.foody.R
import com.example.foody.ui.components.FoodyButton

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CardProductScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    id: Int = 1,
) {
    val product = viewModel.catalogUiState.collectAsState().value.productList.first { it.id == id }
//    val product = viewModel.productFlow.collectAsState().value.first { it.id == id }
    Scaffold(
        bottomBar = {
            FoodyButton(
                onClick = {
                    viewModel.saveInCart(id, product.count + 1)
                    navController.navigate("ShoppingCart/${id}")
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                content = {
                    Text(text = "В корзину за ${product.price_current} ₽", fontSize = 16.sp)
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                GlideImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = Uri.parse(stringResource(id = R.string.picture_url)),
                    contentDescription = null
                )
            }
            item {
                Text(text = product.name, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp,24.dp))
            }
            item {
                Text(text = product.description,Modifier.padding(16.dp), color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f))
            }

            itemsIndexed(
                listOf(
                    Pair(R.string.measure, "${product.measure} ${product.measure_unit}"),
                    Pair(R.string.energy_per_100_grams, "${product.energy_per_100_grams}"),
                    Pair(R.string.proteins_per_100_grams, "${product.proteins_per_100_grams}"),
                    Pair(R.string.fats_per_100_grams, "${product.fats_per_100_grams}"),
                    Pair(R.string.carbohydrates_per_100_grams, "${product.carbohydrates_per_100_grams}"),
                )
            ) { _, item ->
                CardProductItem(propertyNameId = item.first, properties = item.second)
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
    propertyNameId: Int,
    properties: String,
) {
    Divider()
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 13.dp)) {
        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = stringResource(id = propertyNameId), color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = properties)
    }
}

@Preview
@Composable
fun CardProductItemPreview() {
    CardProductItem(propertyNameId = R.string.measure, properties = "1 кг")
}