package com.adjarabet.user.utils

import java.lang.Exception

sealed class Result<out T> {

    data class Success<T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[error=$throwable]"
            Loading -> "Loading..."
        }
    }
}
