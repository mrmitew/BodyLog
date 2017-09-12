package com.github.mrmitew.bodylog.framework.common.view

import android.os.Bundle
import com.github.mrmitew.bodylog.adapter.common.UiState

import com.github.mrmitew.bodylog.adapter.common.view.BaseView
import com.github.mrmitew.bodylog.framework.common.presenter.BasePresenterHolder

abstract class BasePresentableActivity<V : BaseView<S>, S : UiState> : InjectableActivity(), Presentable<V, S> {
    private lateinit var presenterHolder: BasePresenterHolder<V, S>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPresenterHolder()
    }

    private fun setPresenterHolder() {
        presenterHolder = injectPresenterHolder()
    }

    override fun onStart() {
        super.onStart()
        presenterHolder.onAttachedToWindow(view)
    }

    override fun onStop() {
        super.onStop()
        presenterHolder.onDetachedFromWindow()
    }
}