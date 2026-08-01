package me.ghostbear.coauthoredby.core.http

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

@BindingContainer
@ContributesTo(AppScope::class)
object HttpProvidersContainer {

    @Provides
    fun httpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json()
            }

            defaultRequest {
                url("https://api.github.com")
                header("Accept", "application/vnd.github+json")
                header("X-GitHub-Api-Version", "2026-03-10")
            }
        }
    }

}
