package org.calamarfederal.messydiet.feature.search.data.model

import org.calamarfederal.messydiet.food.data.central.model.FoodDataCentralError


/**
 * All known non-fatal errors for this module
 */
sealed interface SearchRemoteError {
    data class UnknownNetworkError(
        override val message: String?,
        val code: Int,
    ) : SearchRemoteError

    data class NetworkTimeoutError(
        override val message: String?,
    ) : SearchRemoteError

    data class NotFoundError(
        override val message: String?,
    ) : SearchRemoteError

    data class OverRateLimitError(
        override val message: String?,
    ) : SearchRemoteError

    data class InvalidFoodIdError(
        override val message: String?,
        val id: Int,
        val type: Int,
    ) : SearchRemoteError

    data class InternalApiError(
        override val message: String?,
        val cause: Throwable? = null,
    ) : SearchRemoteError

    val message: String?
}

/**
 * Fetch (remote) details on a known food item
 */

internal fun FoodDataCentralError.toSearchRemoteError(): SearchRemoteError = when (this) {
    is FoodDataCentralError.NotFoundError -> SearchRemoteError.NotFoundError(message)
    is FoodDataCentralError.OverRateLimitError -> SearchRemoteError.OverRateLimitError(message)
    is FoodDataCentralError.NetworkError -> SearchRemoteError.UnknownNetworkError(message, code)
    is FoodDataCentralError.ParseErrorType -> SearchRemoteError.InternalApiError(message = message, cause = this)
}
