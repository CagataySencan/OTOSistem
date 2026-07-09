package com.cagataysencan.otosistem.core.network.result

import retrofit2.HttpException
import java.io.IOException

/**
 * Wraps API call outcomes. Use [safeApiCall] in remote data sources before mapping to domain models.
 */
enum class NetworkErrorKind {
    NETWORK,
    UNKNOWN,
}

sealed class NetworkResponse<out T> {

    /** API call completed successfully with [data]. */
    data class Success<T>(val data: T) : NetworkResponse<T>()

    /** API call failed with an optional HTTP [code] and [message]. */
    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val kind: NetworkErrorKind? = null,
    ) : NetworkResponse<Nothing>()
}

/**
 * Executes [apiCall] and maps exceptions to [NetworkResponse.Error].
 * Use in remote data sources to avoid try/catch boilerplate.
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResponse<T> {
    return try {
        NetworkResponse.Success(apiCall())
    } catch (exception: HttpException) {
        NetworkResponse.Error(
            code = exception.code(),
            message = exception.message(),
        )
    } catch (exception: IOException) {
        NetworkResponse.Error(
            message = exception.message,
            kind = NetworkErrorKind.NETWORK,
        )
    } catch (exception: Exception) {
        NetworkResponse.Error(
            message = exception.message,
            kind = NetworkErrorKind.UNKNOWN,
        )
    }
}
