package com.github.mrmitew.bodylog.adapter.common.view

import com.github.mrmitew.bodylog.adapter.common.UiState

class EmptyView<S : UiState> : BaseView<S> {
    override fun render(state: S) {
        // No-op
    }
}