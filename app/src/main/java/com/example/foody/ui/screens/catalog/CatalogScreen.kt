package com.example.foody.ui.screens.catalog

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foody.MainViewModel
import com.example.foody.R
import com.example.foody.data.models.products.ProductsItem
import com.example.foody.ui.components.AddToCartBottomBar
import com.example.foody.ui.components.CardButton
import com.example.foody.ui.components.CategoriesRowItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatalogScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    catalogState: LazyGridState,
    coroutineScope: CoroutineScope,
) {
    val uiState = viewModel.catalogUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            LazyRow(
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                    )
            ) {
                itemsIndexed(uiState.value.categories) { _, item ->
                    CategoriesRowItem(
                        tagName = item.name,
                        clearChangedCategoryId = {
                            viewModel.clearChangedCategoryId()
                            coroutineScope.launch {
                                catalogState.animateScrollToItem(0)
                            }
                        },
                        onClickCategories = {
                            viewModel.changeChangedCategoryId(item.id)
                            coroutineScope.launch {
                                catalogState.animateScrollToItem(0)
                            }
                        },
                        selected = uiState.value.selectedCategoryId == item.id
                    )
                }
            }
        },
        bottomBar = {
            AddToCartBottomBar(
                cartPrice = uiState.value.cartPrice,
                goToShoppingCart = {
                    navController.navigate("ShoppingCart/-1")
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            state = catalogState,
        ) {
            itemsIndexed(
                items = uiState.value.filteredProductList,
                key = { _, item -> item.id }
            ) { _, item ->
                ItemCard(
                    Modifier
                        .animateItemPlacement()
                        .padding(top = 8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.medium
                        ),
                    productItem = item,
                    saveInCart = viewModel::saveInCart,
                    goToCardProductScreen = { productId -> navController.navigate("CardProduct/${productId}") }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCard(
    modifier: Modifier,
    productItem: ProductsItem,
    saveInCart: (productId: Int, count: Int) -> Unit,
    goToCardProductScreen: (productId: Int) -> Unit,
) {
    Column(
        modifier = modifier
    ) {

        GlideImage(
            modifier = Modifier.clickable {
                goToCardProductScreen(productItem.id)
            },
            model = Uri.parse(stringResource(id = R.string.picture_url)),
            contentDescription = null
        )

        Text(
            text = productItem.name,
            modifier = Modifier.padding(12.dp, 0.dp),
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
        )
        Text(
            text = "${productItem.measure} ${productItem.measure_unit}", modifier = Modifier
                .padding(12.dp, 0.dp)
                .fillMaxHeight(), fontSize = 14.sp,
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
        )
        CardButton(
            priceCurrent = productItem.price_current,
            priceOld = productItem.price_old,
            productId = productItem.id,
            saveInCart = saveInCart,
            count = productItem.count
        )
    }
}

@Preview
@Composable
fun ItemCardPreview() {
    ItemCard(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            ),
        productItem = ProductsItem(
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
        goToCardProductScreen = { _: Int -> }
    )
}
