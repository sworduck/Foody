package com.example.foody.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddToCartCatalogItemButton(
    modifier: Modifier,
    priceCurrent: Int,
    priceOld: Int,
    onClickAddToCart: () -> Unit,
) {
    FoodyButton(
        onClick = { onClickAddToCart() },
        modifier = modifier,
        content = {
            Row {
                Text(text = "$priceCurrent ₽", modifier = Modifier, fontSize = 16.sp)
                if (priceOld != 0)
                    Text(
                        text = "$priceOld ₽",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp),
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
                    )
            }
        }
    )
}

@Preview
@Composable
fun AddToCartButtonPreview() {
    AddToCartCatalogItemButton(modifier = Modifier, priceCurrent = 100, priceOld = 100, onClickAddToCart = {})
}