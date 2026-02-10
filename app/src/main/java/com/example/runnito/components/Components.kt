package com.example.runnito.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest.Builder
import coil3.request.crossfade
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.example.runnito.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RunningManLoader() {
    val dynamicColor = Color.Blue

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = dynamicColor.toArgb(),
            keyPath = arrayOf("**")
        ),
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

@Composable
fun ImageBanner(modifier: Modifier = Modifier, url: String?) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = "Banner Image",
        modifier = modifier
            .height(220.dp)
            .clip(
                RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            ),
        contentScale = ContentScale.Fit,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        error = {
            Image(
                painter = painterResource(id = R.drawable.not_available),
                contentDescription = "Error loading image"
            )
        },
        success = {
            SubcomposeAsyncImageContent()
        }
    )
}

@Composable
fun ProfileImage(imageUri: String?, onImageClick: () -> Unit = {}) {
    val imageModifier = Modifier
        .size(150.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        )
        .clickable { onImageClick() }

    Box(
        modifier = imageModifier,
        contentAlignment = Alignment.Center
    ) {
        if (imageUri == null || imageUri.isBlank()) {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = "Upload Profile Picture",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(
                    Builder(LocalContext.current)
                        .data(data = Uri.parse(imageUri))
                        .crossfade(true)
                        .build()
                ),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunnitoAppBar(
    title: String,
    showBackButton: Boolean,
    onBackButtonClicked: () -> Unit,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { onBackButtonClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                IconButton(onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        }
    )
}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search races...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )
}