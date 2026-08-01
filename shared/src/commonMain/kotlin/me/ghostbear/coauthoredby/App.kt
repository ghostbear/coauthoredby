package me.ghostbear.coauthoredby

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.browser.window
import me.ghostbear.coauthoredby.presentation.CoAuthoredByScreen
import org.w3c.dom.MediaQueryListEvent

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = appColorScheme()
    ) {
        CoAuthoredByScreen()
    }
}

@Composable
fun appColorScheme(): ColorScheme {
    var isDarkMode by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val matcher = window.matchMedia("(prefers-color-scheme: dark)")
        isDarkMode = matcher.matches
        matcher.addEventListener("change") { event ->
            val event = event as MediaQueryListEvent
            isDarkMode = event.matches
        }
    }

    return if (isDarkMode) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }
}