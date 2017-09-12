package com.github.mrmitew.bodylog.framework.common.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

import com.github.mrmitew.bodylog.adapter.common.UiState
import com.github.mrmitew.bodylog.adapter.common.view.BaseView
import com.github.mrmitew.bodylog.framework.common.presenter.BasePresenterHolder


abstract class BasePresentableTextView<V : BaseView<S>, S : UiState> : AppCompatTextView, Presentable<V, S> {
    private lateinit var mPresenterHolder: BasePresenterHolder<V, S>

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        if (!isInEditMode) {
            setPresenterHolder()
        }
    }

    private fun setPresenterHolder() {
        mPresenterHolder = injectPresenterHolder()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mPresenterHolder.onAttachedToWindow(view)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mPresenterHolder.onDetachedFromWindow()
    }
}
