package com.example.foody.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FoodyButton(
    onClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit),
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Button(
        contentPadding = PaddingValues(12.dp),
        onClick = { onClick() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = MaterialTheme.shapes.medium,
        content = content
    )
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
fun FoodyButtonPreview() {
    FoodyButton(
        onClick = {},
        content = {
                  Text(text = "Добавить в корзину")
        },
        modifier = Modifier
    )
}