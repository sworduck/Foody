package com.example.foody.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CategoriesRowItem(
    tagName: String,
    onClickCategories: () -> Unit,
    selected: Boolean = false,
    clearChangedCategoryId: () -> Unit
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
                if (selected) {
                    clearChangedCategoryId()
                } else {
                    onClickCategories()
                }
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = tagName, modifier = Modifier.padding(16.dp, 12.dp),
            color = if (selected) Color.White else Color.Black
        )
    }
}

@Preview
@Composable
fun CategoriesRowItemPreview() {
    CategoriesRowItem(tagName = "Холодные закуски", onClickCategories = {}, selected = true, clearChangedCategoryId = {})
}