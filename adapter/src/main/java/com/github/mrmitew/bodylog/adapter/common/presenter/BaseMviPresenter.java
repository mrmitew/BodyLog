package com.github.mrmitew.bodylog.adapter.common.presenter;

import com.github.mrmitew.bodylog.adapter.common.model.PartialState;
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent;
import com.github.mrmitew.bodylog.adapter.common.view.BaseView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseMviPresenter<S> extends BasePresenter {
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

    protected abstract Observable<PartialState> createPartialStateObservable(final Observable<UIIntent> uiIntentObservable);

    protected abstract S createState(final S previousState, final PartialState partialState);

    protected abstract S getInitialState();

    protected abstract Observable<UIIntent> getViewIntents();

    protected abstract BaseView<S> getView();

    protected BaseMviPresenter() {
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
                .subscribe(getView()::render, throwable -> {
                    throw new RuntimeException(throwable);
                }));
    }

    protected void bindInternalIntents() {
    }

    protected Observable<S> reduce(Observable<UIIntent> uiIntentObservable) {
        return uiIntentObservable
                .compose(this::createPartialStateObservable)
                .scan(getInitialState(), this::createState)
                .doOnNext(System.out::println);
    }


    public void detachView() {
        mViewGateways.clear();
    }

    @Override
    public void dispose() {
        super.dispose();
        mModelGateways.dispose();
        mViewGateways.dispose();
    }
}
