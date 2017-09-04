package com.github.mrmitew.bodylog.adapter.common.presenter;

import com.github.mrmitew.bodylog.adapter.common.view.BaseView;

public interface HasDetachableView<V extends BaseView> {
    void attachView(V view);

    void detachView();
}
