package com.example.foody.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.foody.MainViewModel
import com.example.foody.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.foody.data.models.tags.TagsItem
import com.example.foody.ui.screens.CardProductScreen
import com.example.foody.ui.screens.CatalogScreen
import com.example.foody.ui.screens.FoodyButton
import com.example.foody.ui.screens.ShoppingCartScreen


enum class FoodyScreen(val title: String) {
    ShoppingCart(title = "ShoppingCart"),
    CardProduct(title = "CardProduct/{productId}"),
    Catalog(title = "Catalog"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupcakeAppBar(
    currentScreen: String?,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    tagList: List<TagsItem>,
    changeTag: (Int, Boolean) -> Unit,
    countOfSelectedTags: () -> Int,
    filterFromSearchProducts: (String) -> Unit,
    filterFromTagsProducts: () -> Unit,
) {
    var showSheet = remember { mutableStateOf(false) }
    var showSearchAppBar = remember { mutableStateOf(false) }
    when {
        currentScreen?.contains("Catalog") ?:false -> {
            if (showSheet.value) {
                BottomSheet(
                    onDismiss = { showSheet.value = false },
                    tagList = tagList,
                    changeTag = changeTag,
                    filterFromTagsProducts = filterFromTagsProducts
                )
            }
            if (showSearchAppBar.value) {
                SearchAppBar(
                    showMainAppBar = { showSearchAppBar.value = false },
                    filterFromSearchProducts = filterFromSearchProducts,
                    filterFromTagsProducts = filterFromTagsProducts
                )
            } else {
                MainAppBar(
                    canNavigateBack = canNavigateBack,
                    modifier = modifier,
                    countOfSelectedTags = countOfSelectedTags,
                    showSheet = { showSheet.value = true },
                    showSearchAppBar = { showSearchAppBar.value = true }
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
                            contentDescription = ""
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
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen =
        backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                tagList = viewModel.tagFlow.collectAsState().value,
                changeTag = viewModel::changeTag,
                countOfSelectedTags = viewModel::getCountOfSelectedTags,
                filterFromSearchProducts = viewModel::filterFromSearchProducts,
                filterFromTagsProducts = viewModel::filterFromTagsProducts
            )
        }
    ) { innerPadding ->
        //val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = FoodyScreen.Catalog.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(FoodyScreen.ShoppingCart.title) {
                ShoppingCartScreen(
                    viewModel = viewModel,
                    navController = navController,
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
                    /*categories = viewModel.categoriesFlow.collectAsState().value,
                    productList = viewModel.filteredProductFlow,
                    saveInCart = viewModel::saveInCart*/
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    tagList: List<TagsItem>,
    changeTag: (Int, Boolean) -> Unit,
    filterFromTagsProducts: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(height = 0.dp) },
    ) {
        Text(
            text = "Подобрать блюда", modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .align(Alignment.CenterHorizontally), fontSize = 24.sp
        )
        repeat(tagList.size) {
            BottomSheetCheckBox(
                tagsItem = tagList[it],
                changeTag = changeTag
            )
            if (it != tagList.lastIndex)
                Divider()
        }
        FoodyButton(
            onClick = {
                filterFromTagsProducts()
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 12.dp)
            ,
            content = {
                Text(text = "Готово")
            }
        )
    }
}

@Composable
fun BottomSheetCheckBox(
    tagsItem: TagsItem,
    changeTag: (Int, Boolean) -> Unit,
) {
    val (checkedState, onStateChange) = remember { mutableStateOf(tagsItem.selected) }
    if (checkedState) changeTag(tagsItem.id, true)
    else changeTag(tagsItem.id, false)
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .toggleable(
                value = checkedState,
                onValueChange = { onStateChange(!checkedState) },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = null // null recommended for accessibility with screenreaders,

        )
        Text(
            text = tagsItem.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    showMainAppBar: () -> Unit,
    filterFromSearchProducts: (String) -> Unit,
    filterFromTagsProducts: () -> Unit,
) {
    var query: String by rememberSaveable { mutableStateOf("") }
    var showClearIcon by rememberSaveable { mutableStateOf(false) }
    if (query.isEmpty()) {
        showClearIcon = false
    } else if (query.isNotEmpty()) {
        showClearIcon = true
    }
    TextField(
        modifier = Modifier.fillMaxWidth().background(Color.White),
        value = query,
        onValueChange = { onQueryChanged ->
            // If user makes changes to text, immediately update it.
            query = onQueryChanged
            // Perform query only when string isn't empty.
            filterFromSearchProducts(onQueryChanged)

        },
        leadingIcon = {
            IconButton(onClick = {
                filterFromTagsProducts()
                showMainAppBar()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
            }
        },
        trailingIcon = {
            if (showClearIcon) {
                IconButton(onClick = {
                    query = ""
                    filterFromTagsProducts()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Clear icon"
                    )
                }
            }
        },
        placeholder = { Text(text = "Найти блюдо") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    modifier: Modifier,
    canNavigateBack: Boolean,
    countOfSelectedTags: () -> Int,
    showSearchAppBar: () -> Unit,
    showSheet: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Icon(painter = painterResource(id = R.drawable.logo), contentDescription = "") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                /*IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }*/
            }

            BadgedBox(
                badge = {
                    Badge {
                        val badgeNumber = countOfSelectedTags().toString()
                        Text(
                            badgeNumber,
                            modifier = Modifier.semantics {
                                contentDescription = "$badgeNumber new notifications"
                            }
                        )
                    }
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource(),
                            onClick = {
                                showSheet()
                            }
                        )
                        .padding(start = 8.dp)
                )
            }

        },
        actions = {
            /*OutlinedTextField(
                value = "",//mainViewModel.searchText.value,
                onValueChange = {
                    //mainViewModel.searchText.value = it
                }
            )*/
            //Icon(painter = painterResource(id = R.drawable.logo), contentDescription = "")
            /*Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "",
                modifier = Modifier.clickable {

                })*/
            IconButton(onClick = { showSearchAppBar() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            }
        }
    )
}
