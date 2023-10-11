package com.example.foody.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foody.MainViewModel
import com.example.foody.data.models.tags.TagsItem
import com.example.foody.ui.components.BottomSheet
import com.example.foody.ui.components.MainAppBar
import com.example.foody.ui.components.SearchAppBar
import com.example.foody.ui.screens.CardProductScreen
import com.example.foody.ui.screens.ShoppingCartScreen
import com.example.foody.ui.screens.catalog.CatalogScreen
import kotlinx.coroutines.launch


enum class FoodyScreen(val title: String) {
    ShoppingCart(title = "ShoppingCart/{newProducts}"),
    CardProduct(title = "CardProduct/{productId}"),
    Catalog(title = "Catalog"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupcakeAppBar(
    canNavigateBack: Boolean,
    currentScreen: String?,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    tagList: List<TagsItem>,
    changeTag: (Int, Boolean) -> Unit,
    filterFromSearchProducts: (String) -> Unit,
    filterFromTagsProducts: () -> Unit,
    clearSearchProducts: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var showSearchAppBar by remember { mutableStateOf(false) }
    when {
        currentScreen?.contains("Catalog") ?:false -> {
            if (showSheet) {
                BottomSheet(
                    onDismiss = { showSheet = false },
                    tagList = tagList,
                    changeTag = changeTag,
                    filterFromTagsProducts = filterFromTagsProducts
                )
            }
            if (showSearchAppBar) {
                SearchAppBar(
                    showMainAppBar = { showSearchAppBar = false },
                    filterFromSearchProducts = filterFromSearchProducts,
                    filterFromTagsProducts = filterFromTagsProducts,
                    clearSearchProducts = clearSearchProducts
                )
            } else {
                MainAppBar(
                    modifier = modifier,
                    countOfSelectedTags = { tagList.count { it.selected } },
                    showSheet = { showSheet = true },
                    showSearchAppBar = { showSearchAppBar = true }
                )
            }
        }
        currentScreen?.contains("CardProduct") ?: false -> {
        }

        currentScreen?.contains("ShoppingCart") ?: false -> {
            TopAppBar(
                title = { Text(text = "Корзина")},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if(canNavigateBack)
                                navigateUp()
                        },
                    ){
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun FoodyApp(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        backStackEntry?.destination?.route
    val catalogState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                tagList = viewModel.tagFlow.collectAsState().value,
                changeTag = viewModel::changeTag,
                filterFromSearchProducts = viewModel::filterFromSearchProducts,
                filterFromTagsProducts = viewModel::filterFromTagsProducts,
                clearSearchProducts = {
                    coroutineScope.launch {
                        catalogState.animateScrollToItem(0)
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FoodyScreen.Catalog.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = FoodyScreen.ShoppingCart.title,
                arguments = listOf(navArgument("newProducts") {
                    type = NavType.StringType
                })
            ) {
                ShoppingCartScreen(
                    viewModel = viewModel,
                    newProducts = it.arguments?.getString("newProducts") ?: "-1"
                )
            }
            composable(
                route = FoodyScreen.CardProduct.title,
                arguments = listOf(navArgument("productId") {
                    type = NavType.StringType
                })
            ) {

                CardProductScreen(
                    viewModel = viewModel,
                    navController = navController,
                    id =(it.arguments?.getString("productId") ?: "0").toInt()
                )
            }
            composable(FoodyScreen.Catalog.title) {

                CatalogScreen(
                    viewModel = viewModel,
                    navController = navController,
                    catalogState = catalogState,
                    coroutineScope = coroutineScope
                )
            }
        }
    }
}