package com.muhammedbuga.yemektarifleri.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.muhammedbuga.yemektarifleri.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationEnd: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))

    LaunchedEffect(Unit) {
        delay(3000) // 3 saniye bekle
        onAnimationEnd()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimation(composition)
    }
}
