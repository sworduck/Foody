package com.example.foody.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foody.MainViewModel
import com.example.foody.R
import com.example.foody.data.models.products.ProductsItem
import com.example.foody.ui.FoodyScreen
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CatalogScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val categories = viewModel.categoriesFlow.collectAsState()
    val productList = viewModel.filteredProductFlow.collectAsState()
    val selectedCategoryId = viewModel.selectedCategory.collectAsState()
    val cartPrice = viewModel.currentCartPrice.collectAsState()
    Scaffold(
        topBar = {
            LazyRow(
                Modifier
                    .background(
                        color = Color.White,
                    )
            ) {
                itemsIndexed(categories.value) { _, item ->
                    CategoriesRowItem(
                        tagName = item.name,
                        onClickCategories = {
                            viewModel.changeChangedCategoryId(item.id)
                        },
                        selected = selectedCategoryId.value == item.id
                    )
                }
            }
        },
        bottomBar = {
            BottomBar(
                cartPrice = cartPrice.value,
                goToShoppingCart = {
                    navController.navigate("ShoppingCart")
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
        ) {
            itemsIndexed(productList.value) { _, item ->
                ItemCard(
                    Modifier.padding(innerPadding),
                    productItem = item,
                    saveInCart = viewModel::saveInCart,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun CategoriesRowItem(
    tagName: String,
    onClickCategories: () -> Unit,
    selected: Boolean = false,
) {

    Box(
        Modifier
            .padding(8.dp)
            .background(
                color = if (selected)
                    MaterialTheme.colorScheme.primary
                else Color.White,
                shape = MaterialTheme.shapes.medium,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.White),
            ) {
                onClickCategories()
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = tagName, modifier = Modifier.padding(16.dp, 12.dp),
            color = if (selected) Color.White else Color.Black
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCard(
    modifier: Modifier,
    productItem: ProductsItem,
    saveInCart: (productId: Int, count: Int) -> Unit,
    navController: NavHostController
) {
    Column(
        Modifier
            .padding(top = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
    ) {

        GlideImage(
            modifier = Modifier.clickable {
                navController.navigate("CardProduct/${productItem.id}")
            },//CardProduct/{productId}
            model = Uri.parse("file:///android_asset/1.png"),
            contentDescription = null
        )

        Text(
            text = "${productItem.name}",
            modifier = Modifier.padding(12.dp, 0.dp),
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
        )
        Text(
            text = "${productItem.measure} ${productItem.measure_unit}", modifier = Modifier
                .padding(12.dp, 0.dp)
                .fillMaxHeight(), fontSize = 14.sp
        )
        CardButton(
            price_current = productItem.price_current,
            price_old = productItem.price_old,
            productId = productItem.id,
            saveInCart = saveInCart,
            count = productItem.count
        )
    }
}

@Composable
fun CardButton(
    price_current: Int,
    price_old: Int,
    productId: Int,
    saveInCart: (productId: Int, count: Int) -> Unit,
    count: Int,
) {
    val currentCount = remember { mutableIntStateOf(count) }
    if (currentCount.value > 0) {
        Counter(
            changeCount = { newCount ->
                saveInCart(productId, newCount)
                currentCount.intValue = newCount
            },
            currentCount.value
        )
        Log.i("ALOALO", productId.toString())
    } else {
        AddToCartButton(
            price_current = price_current,
            price_old = price_old,
            onClickAddToCart = {
                currentCount.intValue++
                saveInCart(productId, 1)
            }
        )
    }

}

@Composable
fun AddToCartButton(
    price_current: Int,
    price_old: Int,
    onClickAddToCart: () -> Unit,
) {
    FoodyButton(
        onClick = { onClickAddToCart() },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        content = {
            Row() {
                Text(text = "$price_current ₽", modifier = Modifier, fontSize = 16.sp)
                if (price_old != 0)
                    Text(
                        text = "$price_old ₽",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp),
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
            }
        }
    )
}


@Composable
fun Counter(
    changeCount: (count: Int) -> Unit,
    count: Int,
) {
    val number = remember { mutableIntStateOf(count) }
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Button(
            onClick = {
                number.intValue++
                changeCount(number.intValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Red
            )
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_plus), contentDescription = "")
        }
        Text("${number.intValue}")
        Button(
            onClick = {
                number.intValue--
                changeCount(number.intValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Red
            )
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_minus), contentDescription = "")
        }
    }
}

@Composable
fun BottomBar(
    cartPrice: Int,
    goToShoppingCart: () -> Unit
) {
    FoodyButton(
        onClick = {
            goToShoppingCart()
        },
        content = {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "ShoppingCart")
            Text(text = "$cartPrice ₽", fontSize = 16.sp)
        },
        modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun FoodyButton(
    onClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit),
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
        content = content
    )
}

