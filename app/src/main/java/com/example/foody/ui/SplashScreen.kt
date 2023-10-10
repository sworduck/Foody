package com.example.foody.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foody.MainViewModel
import com.example.foody.R

@Composable
fun SplashScreen(
    viewModel: MainViewModel = viewModel()
) {
    viewModel.getTags()
    viewModel.getProducts()
    viewModel.getCategories()
    viewModel.filterFromTagsProducts()
    viewModel.getCartPrice()
    val spec = LottieCompositionSpec.RawRes(R.raw.loading_animation)
    val composition by rememberLottieComposition(spec)
    val compositionResult by animateLottieCompositionAsState(composition)

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        LottieAnimation(
            composition,
            iterations = 1,
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