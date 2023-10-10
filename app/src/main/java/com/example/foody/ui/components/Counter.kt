package com.example.foody.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foody.R

@Composable
fun Counter(
    modifier: Modifier,
    changeCount: (count: Int) -> Unit,
    count: Int,
    buttonBackGroundColor: Color
) {
    val number = remember { mutableIntStateOf(count) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            contentPadding = PaddingValues(14.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                number.intValue++
                changeCount(number.intValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonBackGroundColor,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_plus), contentDescription = "")
        }
        AnimatedCounter(count = number.intValue, modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            contentPadding = PaddingValues(14.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                number.intValue--
                changeCount(number.intValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonBackGroundColor,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_minus), contentDescription = "")
        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
fun CounterPreview() {
    Counter(
        modifier = Modifier,
        changeCount = {},
        count = 0,
        buttonBackGroundColor = MaterialTheme.colorScheme.primaryContainer
    )
}