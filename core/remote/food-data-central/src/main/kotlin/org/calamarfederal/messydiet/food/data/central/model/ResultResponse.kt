package org.calamarfederal.messydiet.food.data.central.model

/**
 * Basically a parody of kotlin's Result but with a custom fail type
 */
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

sealed interface FoodDataCentralError {
    sealed class ParseErrorType : FoodDataCentralError, Throwable()

    data class UnrecognizedWeightUnitFormat(
        val input: String,
        override val message: String? = "Unrecognized weight unit: '$input'",
    ) : ParseErrorType()

    data class UnrecognizedNutrientNumber(
        val number: String,
        override val message: String? = "Unsupported nutrient number: '$number'",
    ) : ParseErrorType()

    data class UnrecognizedNutrientDerivation(
        val code: String,
        val description: String?,
        override val message: String? = "Unrecognized nutrient derivation: code = '$code' : description = '$description'",
    ) : ParseErrorType()

    sealed interface RemoteErrorType : FoodDataCentralError
    data class NetworkError(
        override val message: String?,
        val code: Int,
    ) : FoodDataCentralError, RemoteErrorType

    data class NotFoundError(
        override val message: String?,
    ) : FoodDataCentralError, RemoteErrorType {
        val code = 404
    }

    data class OverRateLimitError(
        override val message: String?,
    ) : FoodDataCentralError, RemoteErrorType {
        val code = 429
    }

    val message: String?

    companion object
}

fun FoodDataCentralError.Companion.fromCode(code: Int, message: String?): FoodDataCentralError = when (code) {
    404 -> FoodDataCentralError.NotFoundError(message)
    429 -> FoodDataCentralError.OverRateLimitError(message)
    else -> FoodDataCentralError.NetworkError(message, code)
}
