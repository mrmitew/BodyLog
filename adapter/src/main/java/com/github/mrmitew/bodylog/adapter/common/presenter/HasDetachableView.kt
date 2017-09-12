package com.github.mrmitew.bodylog.adapter.common.presenter

import com.github.mrmitew.bodylog.adapter.common.view.BaseView

interface HasDetachableView<V : BaseView<*>> {
    fun attachView(view: V)

    fun detachView()
}
