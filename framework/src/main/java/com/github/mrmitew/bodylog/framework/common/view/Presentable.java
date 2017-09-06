package com.github.mrmitew.bodylog.framework.common.view;

import com.github.mrmitew.bodylog.adapter.common.view.BaseView;
import com.github.mrmitew.bodylog.framework.common.presenter.BasePresenterHolder;

public interface Presentable<V extends BaseView<S>, S> {
    V getView();

    BasePresenterHolder<V, S> injectPresenterHolder();
}
