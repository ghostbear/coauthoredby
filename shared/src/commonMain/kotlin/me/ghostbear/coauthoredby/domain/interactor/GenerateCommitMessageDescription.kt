package me.ghostbear.coauthoredby.domain.interactor

import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import me.ghostbear.coauthoredby.data.github.UsersDataSource

@Inject
class GenerateCommitMessageDescription(private val usersDataSource: UsersDataSource) {

    suspend operator fun invoke(params: Params): Result {

        try {
            return Result.Success(
                coroutineScope {
                    params.usernames
                        .map { username ->
                            async {
                                username to usersDataSource.getUser(username)
                            }
                        }
                        .awaitAll()
                        .joinToString("\n") { (username, user) ->
                            if (user == null) return@joinToString "Co-authored-by: $username"
                            "Co-authored-by: ${user.name ?: username} <${user.id}+${user.login}@users.noreply.github.com>"
                        }
                }
            )
        } catch (e: Exception) {
            return Result.Error(e.message ?: "Unknown error")
        }
    }

    sealed interface Result {
        data class Success(val value: String) : Result
        data class Error(val message: String) : Result
    }

    data class Params(val usernames: List<String>)
}