package com.github.mrmitew.bodylog.adapter.common.model

abstract class ResultState {
    abstract val isInProgress: Boolean

    abstract val isSuccessful: Boolean

    abstract fun error(): Throwable
}
