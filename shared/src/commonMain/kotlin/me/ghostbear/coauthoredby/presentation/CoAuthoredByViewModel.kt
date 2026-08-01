package me.ghostbear.coauthoredby.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.ghostbear.coauthoredby.core.Optional
import me.ghostbear.coauthoredby.domain.interactor.GenerateCommitMessageDescription
import kotlin.uuid.Uuid

data class CoAuthor(val id: Uuid, val name: String) {
    companion object {
        fun empty() = CoAuthor(Uuid.random(), "")
    }
}

data class CoAuthorByState(
    val coauthors: List<CoAuthor> = listOf(CoAuthor.empty()),
    val focusedId: Optional<Uuid> = Optional.None,
    val commitMessage: Optional<String> = Optional.None,
    val isGenerating: Boolean = false,
)

sealed interface CoAuthorByIntent {
    data class Update(val id: Uuid, val name: String) : CoAuthorByIntent
    data class Delete(val id: Uuid) : CoAuthorByIntent
    data class Focus(val id: Uuid) : CoAuthorByIntent
    data object Unfocus : CoAuthorByIntent
    data object Submit : CoAuthorByIntent
    data class Generated(val message: String) : CoAuthorByIntent
    data class Error(val message: String) : CoAuthorByIntent
    data object Reset : CoAuthorByIntent
    data object Copy : CoAuthorByIntent
    data object Dismiss : CoAuthorByIntent
}

sealed interface CoAuthorByEffect {
    data class Generate(val coauthors: List<CoAuthor>) : CoAuthorByEffect
    data class Copy(val value: String) : CoAuthorByEffect
}

sealed interface CoAuthorByEvent {
    data object Copied : CoAuthorByEvent
    data class Error(val message: String) : CoAuthorByEvent
}

data class Result(
    val state: CoAuthorByState,
    val effect: CoAuthorByEffect?,
)

operator fun CoAuthorByState.unaryPlus(): Result {
    return Result(state = this, effect = null)
}

infix fun Result.with(effect: CoAuthorByEffect): Result {
    return copy(effect = effect)
}

suspend fun Result.withEffect(block: suspend (CoAuthorByEffect) -> Unit) {
    effect?.let { block(it) }
}

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class CoAuthoredByViewModel(
    private val generateCommitMessageDescription: GenerateCommitMessageDescription
) : ViewModel() {

    val events: SharedFlow<CoAuthorByEvent>
        field = MutableSharedFlow(replay = 1)

    val state: StateFlow<CoAuthorByState>
        field = MutableStateFlow(CoAuthorByState())

    val reducer: suspend CoAuthorByState.(CoAuthorByIntent) -> Result =
        reducer@{ intent ->
            when (intent) {
                is CoAuthorByIntent.Update -> {
                    var coauthors =
                        this.coauthors.map { if (it.id == intent.id) CoAuthor(intent.id, intent.name) else it }
                            .toMutableList()
                    val hasEmpty = coauthors.any { it.name.isBlank() }
                    if (!hasEmpty) {
                        coauthors += CoAuthor.empty()
                    }
                    +copy(coauthors = coauthors.toList())
                }

                is CoAuthorByIntent.Delete -> {
                    +copy(coauthors = coauthors.filter { it.id != intent.id })
                }

                is CoAuthorByIntent.Focus -> +copy(focusedId = Optional.Some(intent.id))
                is CoAuthorByIntent.Unfocus -> {
                    if (coauthors.size == 1 && coauthors.first().name.isBlank()) {
                        return@reducer +copy(focusedId = Optional.None)
                    }

                    +copy(
                        coauthors = coauthors.filter { it.name.isNotBlank() } + CoAuthor.empty(),
                        focusedId = Optional.None
                    )
                }

                is CoAuthorByIntent.Submit -> {
                    if (isGenerating) return@reducer +this
                    +copy(isGenerating = true) with CoAuthorByEffect.Generate(coauthors)
                }

                is CoAuthorByIntent.Generated -> {
                    +copy(isGenerating = false, commitMessage = Optional.Some(intent.message))
                }

                is CoAuthorByIntent.Error -> {
                    +copy(isGenerating = false, commitMessage = Optional.None)
                }

                is CoAuthorByIntent.Reset -> {
                    +CoAuthorByState()
                }

                is CoAuthorByIntent.Copy -> {
                    when (val commitMessage = commitMessage) {
                        Optional.None -> +this
                        is Optional.Some<String> -> +copy(commitMessage = Optional.None) with CoAuthorByEffect.Copy(commitMessage.value)
                    }
                }

                is CoAuthorByIntent.Dismiss -> {
                    +copy(commitMessage = Optional.None)
                }
            }
        }

    @OptIn(ExperimentalWasmJsInterop::class)
    fun onIntent(intent: CoAuthorByIntent) {
        viewModelScope.launch {
            val value = state.value
            val result = value.reducer(intent)
            state.value = result.state

            result.withEffect { effect ->
                when (effect) {
                    is CoAuthorByEffect.Copy -> {
                        window.navigator.clipboard.writeText(effect.value).await()
                        events.emit(CoAuthorByEvent.Copied)
                    }
                    is CoAuthorByEffect.Generate -> {
                        val result = generateCommitMessageDescription(
                            GenerateCommitMessageDescription.Params(
                                effect.coauthors
                                    .filter { it.name.isNotBlank() }
                                    .map { it.name }
                            )
                        )
                        when (result) {
                            is GenerateCommitMessageDescription.Result.Error -> {
                                onIntent(CoAuthorByIntent.Error(result.message))
                                events.emit(CoAuthorByEvent.Error(result.message))
                            }
                            is GenerateCommitMessageDescription.Result.Success -> onIntent(CoAuthorByIntent.Generated(result.value))
                        }
                    }
                }
            }
        }
    }

}