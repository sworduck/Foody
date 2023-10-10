package com.example.foody.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foody.ui.FoodyScreen

@Composable
fun AddToCartBottomBar(
    cartPrice: Int,
    goToShoppingCart: () -> Unit,
) {
    FoodyButton(
        onClick = {
            goToShoppingCart()
        },
        content = {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = FoodyScreen.ShoppingCart.title,
                Modifier.size(20.dp)
            )
            Text(text = "$cartPrice ₽", fontSize = 16.sp)
        },
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    )
}

@Preview
@Composable
fun AddToCartBottomBarPreview() {
    AddToCartBottomBar(cartPrice = 100, goToShoppingCart = {})
}