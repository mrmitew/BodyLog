package com.github.mrmitew.bodylog.adapter.common.presenter;

import com.github.mrmitew.bodylog.adapter.common.model.PartialState;
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent;
import com.github.mrmitew.bodylog.adapter.common.view.BaseView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseMviPresenter<V extends BaseView<S>, S> implements BasePresenter, Disposable {
    /**
     * Gateways from views to business logic
     */
    protected final CompositeDisposable mModelGateways;

    /**
     * Gateways from business logic to views
     */
    protected final CompositeDisposable mViewGateways;

    /**
     * Have the model gateways been init already
     */
    private boolean mIsInit;

    //
    // View
    //
    protected V mView;

    protected abstract Observable<PartialState> createPartialStateObservable(final Observable<UIIntent> uiIntentObservable);

    protected abstract S createState(final S previousState, final PartialState partialState);

    protected abstract S getInitialState();

    protected abstract Observable<UIIntent> getViewIntents();

    protected BaseMviPresenter(V view) {
        mView = view;
        mModelGateways = new CompositeDisposable();
        mViewGateways = new CompositeDisposable();
    }

    @Override
    public void bindIntents() {
        if (!mIsInit) {
            bindInternalIntents();
            mIsInit = true;
        }

        mViewGateways.add(reduce(getViewIntents())
                .subscribe(mView::render, throwable -> {
                    throw new RuntimeException(throwable);
                }));
    }


    @Override
    public void unbindIntents() {
        mViewGateways.clear();
    }

    @Override
    public void dispose() {
        mModelGateways.dispose();
        mViewGateways.dispose();
    }

    @Override
    public boolean isDisposed() {
        return mModelGateways.isDisposed() || mViewGateways.isDisposed();
    }

    protected void bindInternalIntents() {
    }

    private Observable<S> reduce(Observable<UIIntent> uiIntentObservable) {
        return uiIntentObservable
                .compose(this::createPartialStateObservable)
                .scan(getInitialState(), this::createState)
                .doOnNext(System.out::println);
    }
}
