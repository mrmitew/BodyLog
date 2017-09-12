package com.github.mrmitew.bodylog.adapter.common.view

import com.github.mrmitew.bodylog.adapter.common.UiState

interface BaseView<S : UiState> {
    fun render(state: S)
}