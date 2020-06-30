package co.tiagoaguiar.atway.core.model

sealed class Response<in T> {
    object Loading : Response<Any>()
    object Loaded : Response<Any>()
    data class Error(val throwable: Throwable) : Response<Any>()
    data class Success<T>(val data: T) : Response<T>()
}