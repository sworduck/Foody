package com.example.foody.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.example.foody.MainViewModel
import com.example.foody.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    viewModel.getTags()
    viewModel.getProducts()
    viewModel.getCategories()
    viewModel.filterFromTagsProducts()
    viewModel.getCartPrice()
    val spec = LottieCompositionSpec.RawRes(R.raw.loading_animation)
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = Color.Green.toArgb(),
            keyPath = arrayOf(
                "H2",
                "Shape 1",
                "Fill 1",
            )
        ),
    )
    val composition by rememberLottieComposition(spec)
    val compositionResult by animateLottieCompositionAsState(composition)

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        LottieAnimation(
            composition,
            iterations = 1,
            dynamicProperties = dynamicProperties,
            clipSpec = LottieClipSpec.Progress(0.0f, 0.80f),
        )
    }

    if(compositionResult > 0.90f){
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
            FoodyApp(
                viewModel = viewModel,
            )
        }
    }
}