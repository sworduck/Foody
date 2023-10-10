package com.example.foody.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CardButton(
    priceCurrent: Int,
    priceOld: Int,
    productId: Int,
    saveInCart: (productId: Int, count: Int) -> Unit,
    count: Int,
) {
    var currentCount by remember { mutableIntStateOf(count) }
    if (currentCount > 0) {
        Counter(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            changeCount = { newCount ->
                saveInCart(productId, newCount)
                currentCount = newCount
            },
            count = currentCount,
            buttonBackGroundColor = MaterialTheme.colorScheme.primaryContainer
        )
    } else {
        AddToCartCatalogItemButton(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            priceCurrent = priceCurrent,
            priceOld = priceOld,
            onClickAddToCart = {
                currentCount++
                saveInCart(productId, 1)
            }
        )
    }
}

@Preview
@Composable
fun CardButtonPreview() {
    CardButton(
        priceCurrent = 100,
        priceOld = 100,
        productId = 1,
        saveInCart = {_, _ ->  },
        count = 1,
    )
}