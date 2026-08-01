package me.ghostbear.coauthoredby

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val appGraph = createGraph<AppGraph>()
    ComposeViewport {
        CompositionLocalProvider(LocalMetroViewModelFactory provides appGraph.metroViewModelFactory) {
            App()
        }
    }
}