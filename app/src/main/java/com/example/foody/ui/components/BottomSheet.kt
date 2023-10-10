package com.example.foody.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foody.R
import com.example.foody.data.models.tags.TagsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    tagList: List<TagsItem>,
    changeTag: (Int, Boolean) -> Unit,
    filterFromTagsProducts: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(height = 0.dp) },
    ) {
        BottomSheetContent(
            onDismiss = onDismiss,
            tagList = tagList,
            changeTag = changeTag,
            filterFromTagsProducts = filterFromTagsProducts,
        )
    }
}

@Composable
fun BottomSheetContent(
    onDismiss: () -> Unit,
    tagList: List<TagsItem>,
    changeTag: (Int, Boolean) -> Unit,
    filterFromTagsProducts: () -> Unit,
){
    Column {
        Text(
            text = stringResource(id = R.string.select_dishes), modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally), fontSize = 24.sp
        )
        repeat(tagList.size) {
            BottomSheetCheckBox(
                tagsItem = tagList[it],
                changeTag = changeTag,
            )
            if (it != tagList.lastIndex)
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
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
                Text(text = stringResource(id = R.string.done_button_text))
            },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Preview
@Composable
fun BottomSheetContentPreview() {
    BottomSheetContent(
        onDismiss = {},
        tagList = listOf(
            TagsItem(
                id = 1,
                name = "Пицца"
            )
        ),
        changeTag = {_, _ ->},
        filterFromTagsProducts = {}
    )
}