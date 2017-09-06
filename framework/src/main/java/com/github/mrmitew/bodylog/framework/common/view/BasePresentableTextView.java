package com.github.mrmitew.bodylog.framework.common.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.github.mrmitew.bodylog.adapter.common.view.BaseView;


public abstract class BasePresentableTextView<V extends BaseView<S>, S> extends AppCompatTextView implements Presentable<V, S> {
    private BasePresenterHolder<V, S> mPresenterHolder;

    public BasePresentableTextView(final Context context) {
        super(context);
        init();
    }

    public BasePresentableTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BasePresentableTextView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if(!isInEditMode()) {
            setPresenterHolder();
        }
    }

    private void setPresenterHolder() {
        mPresenterHolder = injectPresenterHolder();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPresenterHolder.attachView(getView());
        mPresenterHolder.bindIntents();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPresenterHolder.detachView();
    }
}
