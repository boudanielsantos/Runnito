package com.example.runnito.data


data class DataOrException<T, Boolean, Exception>(
    var data: T? = null,
    var loading: Boolean,
    var exception: Exception? = null

)