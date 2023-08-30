package org.calamarfederal.messydiet.food.data.central.model

sealed class ResultResponse<V, R> {
    data class Success<V, R>(val value: V) : ResultResponse<V, R>()
    data class Failure<V, R>(val response: R) : ResultResponse<V, R>()
}

fun <V, R> ResultResponse<V, R>.getValueOrNull(): V? = if (this is ResultResponse.Success) value else null
fun <V, R> ResultResponse<V, R>.getResponseOrNull(): R? = if (this is ResultResponse.Failure) response else null
fun <V, R> ResultResponse<V, R>.isSuccess(): Boolean = this is ResultResponse.Success

fun <V, R, T> ResultResponse<V, R>.fold(
    onSuccess: (V) -> T,
    onFailure: (R) -> T,
): T = when (this) {
    is ResultResponse.Success -> onSuccess(value)
    is ResultResponse.Failure -> onFailure(response)
}

sealed interface FDCError {
    data class NetworkError(
        override val message: String?,
        val code: Int,
    ) : FDCError

    val message: String?
}
