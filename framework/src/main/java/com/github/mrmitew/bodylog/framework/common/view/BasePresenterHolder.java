package com.github.mrmitew.bodylog.framework.common.view;

import android.app.Application;

import com.github.mrmitew.bodylog.adapter.common.presenter.Bindable;
import com.github.mrmitew.bodylog.adapter.common.presenter.DetachableMviPresenter;
import com.github.mrmitew.bodylog.adapter.common.presenter.HasDetachableView;
import com.github.mrmitew.bodylog.adapter.common.view.BaseView;

public abstract class BasePresenterHolder<V extends BaseView<S>, S> extends InjectableViewModel implements Bindable, HasDetachableView<V> {
    public BasePresenterHolder(final Application application) {
        super(application);
    }

    public abstract DetachableMviPresenter<V, S> getPresenter();

    @Override
    protected void onCleared() {
        super.onCleared();
        detachView();
    }

    @Override
    public void attachView(final V view) {
        getPresenter().attachView(view);
    }

    @Override
    public void detachView() {
        getPresenter().detachView();
    }

    @Override
    public void bindIntents() {
        getPresenter().bindIntents();
    }

    @Override
    public void unbindIntents() {
        getPresenter().unbindIntents();
    }
}
