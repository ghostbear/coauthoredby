package me.ghostbear.coauthoredby.core

sealed interface Optional<out T> {
    data object None : Optional<Nothing>
    data class Some<T>(val value: T) : Optional<T>
}

fun <T> Optional<T>.orElse(default: () -> T): T = when (this) {
    is Optional.None -> default()
    is Optional.Some -> value
}