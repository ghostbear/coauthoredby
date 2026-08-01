package me.ghostbear.coauthoredby.data.github

import dev.zacsweers.metro.Inject
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

@Inject
class UsersDataSource(private val httpClient: HttpClient) {

    suspend fun getUser(username: String): User? {
        val response = httpClient.get {
            url {
                path("users/$username")
            }
        }
        if (response.status == HttpStatusCode.NotFound) return null
        return response.body<User>()
    }

}