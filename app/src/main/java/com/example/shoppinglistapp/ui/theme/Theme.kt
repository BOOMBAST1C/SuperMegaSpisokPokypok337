package com.example.shoppinglistapp.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
private val DarkColorScheme = darkColorScheme()
private val LightColorScheme = lightColorScheme()
@Composable
fun ShoppingListAppTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = remember(isDarkTheme) {
        println("Creating new color scheme: $isDarkTheme")
        if (isDarkTheme) DarkColorScheme else LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}