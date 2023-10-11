package com.example.foody.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foody.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    modifier: Modifier,
    countOfSelectedTags: () -> Int,
    showSearchAppBar: () -> Unit,
    showSheet: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Icon(
                painter = painterResource(
                    id = R.drawable.logo
                ),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        ),
        modifier = modifier,
        navigationIcon = {
            BadgedBox(
                badge = {
                    countOfSelectedTags().let{
                        if(it > 0) {
                            Badge {
                                val badgeNumber = it.toString()
                                Text(
                                    badgeNumber,
                                    modifier = Modifier.semantics {
                                        contentDescription = "$badgeNumber new notifications"
                                    }
                                )
                            }
                        }
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
            IconButton(onClick = { showSearchAppBar() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            }
        }
    )
}

@Preview
@Composable
fun MainAppBarPreview() {
    MainAppBar(
        countOfSelectedTags = { 0 },
        showSearchAppBar = {},
        showSheet = {},
        modifier = Modifier
    )
}