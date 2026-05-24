package com.example.lab05.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lab05.R // Este import conecta con tu carpeta res

@Composable
fun ProductIllustration(
    imageType: String,
    modifier: Modifier = Modifier
) {
    // Vinculamos el parámetro string con los archivos reales de tu carpeta drawable
    val imageResId: Int? = when (imageType) {
        "laptop" -> R.drawable.laptop
        "keyboard" -> R.drawable.teclado
        "mouse" -> R.drawable.mouse
        "audio" -> R.drawable.audifonos
        "monitor" -> R.drawable.monitor
        "chair" -> R.drawable.silla
        else -> null
    }

    val gradient = when (imageType) {
        "laptop" -> Brush.linearGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)))
        "keyboard" -> Brush.linearGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)))
        "mouse" -> Brush.linearGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)))
        "audio" -> Brush.linearGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)))
        "monitor" -> Brush.linearGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)))
        "chair" -> Brush.linearGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)))
        else -> Brush.linearGradient(colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        if (imageResId != null) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Imagen de $imageType",
                modifier = Modifier.fillMaxSize(0.7f),
                contentScale = ContentScale.Fit
            )
        } else {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.fillMaxSize(0.45f)
            )
        }
    }
}