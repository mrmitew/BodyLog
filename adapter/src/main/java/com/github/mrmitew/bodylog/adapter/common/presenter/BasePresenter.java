package com.github.mrmitew.bodylog.adapter.common.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter implements Disposable {
    private final CompositeDisposable mCompositeDisposable;

    public BasePresenter() {
        mCompositeDisposable = new CompositeDisposable();
    }

    protected abstract void bindIntents();

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return mCompositeDisposable.isDisposed();
    }

    public void unbindIntents() {
        getCompositeDisposable().clear();
    }
}
