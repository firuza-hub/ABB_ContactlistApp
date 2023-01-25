package com.vholodynskyi.assignment.util

import android.util.Log

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : NetworkResult<T>(data)
    class Error<T>(data: T? = null, message: String) : NetworkResult<T>(data, message)
    class Exception<T>(message: String) : NetworkResult<T>(null, message)
    class Success<T>(data: T? = null) : NetworkResult<T>(data)


    companion object {
        fun <T> handle(
            result: NetworkResult<T>,
            tag: String,
            postValue: (T) -> Unit
        ) {
            when (result) {
                is Success -> {
                    Log.i(tag, "Success")
                    if (result.data != null) postValue(result.data)
                }
                is Error -> Log.i(tag, result.message.toString())
                is Exception -> Log.e(tag, result.message.toString())
                is Loading -> Log.i(tag, "Loading ...")
            }
        }
    }
}