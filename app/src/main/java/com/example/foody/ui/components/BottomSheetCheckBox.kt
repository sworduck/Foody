package com.example.foody.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foody.data.models.tags.TagsItem

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
        Text(
            text = tagsItem.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = checkedState,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.outline,
            )
        )
    }
}

@Preview
@Composable
fun BottomSheetCheckBoxPreview() {
    BottomSheetCheckBox(
        tagsItem = TagsItem(1, "name", true),
        changeTag = {_: Int, _: Boolean -> }
    )
}