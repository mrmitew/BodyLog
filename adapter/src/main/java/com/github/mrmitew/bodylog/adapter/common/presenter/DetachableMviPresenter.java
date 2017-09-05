package com.github.mrmitew.bodylog.adapter.common.presenter;

import com.github.mrmitew.bodylog.adapter.common.view.BaseView;

public abstract class DetachableMviPresenter<V extends BaseView<S>, S> extends BaseMviPresenter<V, S> implements HasDetachableView<V> {
    protected DetachableMviPresenter(final V view) {
        super(view);
    }

    @Override
    public void attachView(final V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        unbindIntents();
    }
}
