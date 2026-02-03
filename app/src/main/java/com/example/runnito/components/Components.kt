package com.example.runnito.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.example.runnito.R

@Composable
fun RunningManLoader() {
    val dynamicColor = Color.Blue

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = dynamicColor.toArgb(),
            keyPath = arrayOf("**")
        ),
        // Attempt to change the Stroke color
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value = dynamicColor.toArgb(),
            keyPath = arrayOf("**")
        )
    )
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.running_loader))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        dynamicProperties = dynamicProperties,
        modifier = Modifier.height(120.dp)
    )
}