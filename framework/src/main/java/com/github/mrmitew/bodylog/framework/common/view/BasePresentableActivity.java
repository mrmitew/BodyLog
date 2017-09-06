package com.github.mrmitew.bodylog.framework.common.view;

import android.os.Bundle;

import com.github.mrmitew.bodylog.adapter.common.view.BaseView;

public abstract class BasePresentableActivity<V extends BaseView<S>, S> extends BaseActivity implements Presentable<V, S> {
    protected BasePresenterHolder<V, S> mPresenterHolder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenterHolder();
    }

    private void setPresenterHolder() {
        mPresenterHolder = injectPresenterHolder();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenterHolder.attachView(getView());
        mPresenterHolder.bindIntents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenterHolder.detachView();
    }
}