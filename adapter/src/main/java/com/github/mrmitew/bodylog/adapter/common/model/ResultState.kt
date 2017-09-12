package com.github.mrmitew.bodylog.adapter.common.model

open class ResultState(open val isInProgress: Boolean, open val isSuccessful: Boolean, open val error: Throwable)

object Factory {
    internal fun inProgress(): ResultState =
            ResultState(
                    isInProgress = true,
                    isSuccessful = false,
                    error = StateError.Empty.INSTANCE)

    internal fun successful(): ResultState =
            ResultState(
                    isInProgress = false,
                    isSuccessful = true,
                    error = StateError.Empty.INSTANCE)

    internal fun error(throwable: Throwable): ResultState =
            ResultState(isInProgress = false,
                    isSuccessful = false,
                    error = throwable)
}