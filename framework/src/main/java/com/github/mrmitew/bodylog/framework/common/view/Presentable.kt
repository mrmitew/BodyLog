package com.github.mrmitew.bodylog.framework.common.view

import com.github.mrmitew.bodylog.adapter.common.UiState
import com.github.mrmitew.bodylog.adapter.common.view.BaseView
import com.github.mrmitew.bodylog.framework.common.presenter.BasePresenterHolder

interface Presentable<V : BaseView<S>, S : UiState> {
    val view: V

    fun injectPresenterHolder(): BasePresenterHolder<V, S>
}
