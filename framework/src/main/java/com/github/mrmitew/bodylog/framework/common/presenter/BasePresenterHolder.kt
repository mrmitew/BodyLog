package com.github.mrmitew.bodylog.framework.common.presenter

import android.app.Application
import com.github.mrmitew.bodylog.adapter.common.UiState

import com.github.mrmitew.bodylog.adapter.common.presenter.Bindable
import com.github.mrmitew.bodylog.adapter.common.presenter.DetachableMviPresenter
import com.github.mrmitew.bodylog.adapter.common.presenter.HasDetachableView
import com.github.mrmitew.bodylog.adapter.common.view.BaseView
import com.github.mrmitew.bodylog.framework.common.viewmodel.InjectableViewModel

abstract class BasePresenterHolder<V : BaseView<S>, S : UiState>(application: Application) : InjectableViewModel(application), Bindable, HasDetachableView<V> {

    abstract val presenter: DetachableMviPresenter<V, S>

    override fun onCleared() {
        super.onCleared()
        onDetachedFromWindow()
    }

    fun onAttachedToWindow(view: V) {
        attachView(view)
        bindIntents()
    }

    fun onDetachedFromWindow() {
        detachView()
        unbindIntents()
    }

    override fun attachView(view: V) {
        presenter.attachView(view)
    }

    override fun detachView() {
        presenter.detachView()
    }

    override fun bindIntents() {
        presenter.bindIntents()
    }

    override fun unbindIntents() {
        presenter.unbindIntents()
    }
}
