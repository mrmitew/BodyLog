package com.github.mrmitew.bodylog.adapter.common.model

open class ResultState(open val isInProgress: Boolean, open val isSuccessful: Boolean, open val error: Throwable)