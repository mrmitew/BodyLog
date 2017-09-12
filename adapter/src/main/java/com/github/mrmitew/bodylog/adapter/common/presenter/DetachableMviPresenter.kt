package com.github.mrmitew.bodylog.adapter.common.presenter

import com.github.mrmitew.bodylog.adapter.common.UiState
import com.github.mrmitew.bodylog.adapter.common.view.BaseView

abstract class DetachableMviPresenter<V : BaseView<S>, S : UiState> protected constructor(view: V) :
        BaseMviPresenter<V, S>(view), HasDetachableView<V> {

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
        unbindIntents()
    }
}
